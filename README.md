Base project for java backend app:

Srping WEB + JWT + JPA + Flyway + Docker

To execute the base app:

```
docker compose down -v                                               
docker compose up app --build -d
```

First command wil refresh postgresql data and the second one will start the app.

You can start only db with

```docker compose up db```

And after start the application locally with:

```./gradlew bootRun --args='--spring.profiles.active=local' ```

The app's login does NOTHING with the user and the password, they should be checked against somethig but is not implemented yet (and I am not sure that I should do it here)

-----------------------------------------------------------

install gradle:

sdk install gradle

build greadlew

gradle wrapper

execute /scripts/run-local.sh

To create aggregate root extend:

import org.springframework.data.domain.AbstractAggregateRoot;
To use it use import org.springframework.context.event.EventListener to mark the method that will receive an spcific type


🗺️ Contexto General: "Viajes Fantásticos"
La aplicación permite a los usuarios (que ya tienes) reservar viajes (paquetes turísticos). 
Tenemos dos grandes responsabilidades que queremos mantener separadas:

Bounded Context: Reservas (Bookings)

Se encarga de todo lo relacionado con la transacción de una reserva: 
quién la hace, para qué viaje, cuántas personas, y en qué estado se encuentra (pendiente, confirmada, cancelada).

Su lenguaje: "Confirmar", "Pasajero", "Estado de la reserva".

Bounded Context: Catálogo (Catalog/Inventory)

Se encarga de la definición de los viajes que se ofrecen: nombre, destino, descripción, y cuántas plazas (asientos) hay disponibles.

Su lenguaje: "Plazas disponibles", "Inventario", "Publicar viaje".

El objetivo del taller es implementar el flujo donde un usuario crea una reserva y, 
una vez confirmada, se descuentan las plazas del inventario de ese viaje en el catálogo.

👣 Paso 1: Vertical Slice (Reservas)
Objetivo: Crear el CRUD de Reservas y la lógica para "confirmar" una reserva, que será nuestro evento de dominio.

Aggregate Root: Booking (Reserva).

Historia de Usuario: "Como usuario, quiero poder crear una reserva para un viaje específico,
indicando el número de pasajeros, para que quede registrada en el sistema en estado 'Pendiente'."

Segunda Historia: "Como usuario, quiero poder confirmar mi reserva (simulando un pago exitoso), para asegurar mis plazas."

Implementación del Taller (Paso 1):
Modelo (Aggregate):

Crean la entidad Booking (el Aggregate Root).

Propiedades: BookingId, UserId (del contexto de usuarios que ya tienes), TripId, NumberOfPassengers, Status (Enum: Pending, Confirmed, Cancelled).

Vertical Slice (CRUD + Lógica):

Crear (C): POST /bookings

Recibe un DTO: { "tripId": "...", "numberOfPassengers": ... }.

El handler crea una nueva instancia de Booking, le asigna el UserId del usuario autenticado, y la guarda en la BBDD con estado Pending.

Leer (R): GET /bookings/{id} o GET /my-bookings

Muestra las reservas del usuario.

Lógica de Negocio (U): POST /bookings/{id}/confirm

Este es el endpoint clave.

El handler carga el Booking desde la BBDD usando su ID.

Invoca un método en el Aggregate Root: booking.Confirmar().

Lógica en Booking.Confirmar():

Valida que el estado actual sea Pending. Si no, lanza una excepción (ej. BookingAlreadyConfirmedException).

Cambia el estado: this.Status = Status.Confirmed.

¡El punto clave! Añade un evento de dominio: this.AddDomainEvent(new BookingConfirmedEvent(this.BookingId, this.TripId, this.NumberOfPassengers)).

Guardado y Publicación:

El repositorio guarda el Booking actualizado en la BBDD.

Un dispatcher (que puedes tener configurado, a veces parte del Unit of Work) detecta los eventos de dominio (BookingConfirmedEvent) y los publica en Kafka (en un topic como bookings.confirmed).

👣 Paso 2: Vertical Slice (Catálogo e Inventario)
Objetivo: Crear el CRUD de Viajes y un listener de Kafka que reaccione al evento de BookingConfirmedEvent para actualizar el inventario.

Aggregate Root: Trip (Viaje).

Historia de Usuario (CRUD): "Como administrador, quiero poder crear y gestionar viajes en el catálogo, definiendo su nombre, destino y el número total de plazas."

Historia de Usuario (Lógica): "Como sistema, cuando una reserva es confirmada, necesito reducir el número de plazas disponibles para ese viaje en el catálogo."

Implementación del Taller (Paso 2):
Modelo (Aggregate):

Crean la entidad Trip (el Aggregate Root).

Propiedades: TripId, Name, Destination, TotalSeats, AvailableSeats.

Vertical Slice (CRUD):

Crear (C): POST /trips (Endpoint de admin)

Recibe un DTO: { "name": "...", "destination": "...", "totalSeats": 100 }.

Crea un nuevo Trip, e inicializa AvailableSeats = TotalSeats.

Leer (R): GET /trips y GET /trips/{id}

Muestra los viajes disponibles (quizás solo los que tengan AvailableSeats > 0).

Actualizar (U): PUT /trips/{id} (Para cambiar el nombre, precio, etc.).

Lógica del Consumidor (Kafka):

Crean un Kafka Consumer (un servicio background o un listener).

Se suscribe al topic: bookings.confirmed.

Lógica del Handler:

Recibe el mensaje BookingConfirmedEvent (con TripId y NumberOfPassengers).

Carga el agregado Trip correspondiente: tripRepository.FindById(event.TripId).

Invoca un método en el Aggregate Root: trip.ReserveSeats(event.NumberOfPassengers).

Lógica en Trip.ReserveSeats(int seatsToReserve):

Valida que haya suficientes plazas: if (this.AvailableSeats < seatsToReserve) { throw new InsufficientSeatsException(...); } (Esto podría generar otro evento de compensación, pero para el taller, con un log es suficiente).

Actualiza el inventario: this.AvailableSeats -= seatsToReserve;.

El handler guarda el Trip actualizado en su propia BBDD.

✨ Resumen del Flujo para el Taller
Admin (Paso 2): Llama a POST /trips para crear el "Viaje a la Luna" con 10 plazas.

Usuario (Paso 1): Llama a POST /bookings para reservar el "Viaje a la Luna" con 2 pasajeros. Se crea la Booking con estado Pending.

Base de Datos 1 (Reservas): Muestra Booking { ..., Status: Pending }.

Base de Datos 2 (Catálogo): Muestra Trip { ..., AvailableSeats: 10 }.

Usuario (Paso 1): Llama a POST /bookings/{id}/confirm.

Contexto Reservas:

El Booking Aggregate cambia su estado a Confirmed.

Publica el evento BookingConfirmedEvent(tripId: "viaje-luna", seats: 2) en Kafka.

Contexto Catálogo (Paso 2):

El listener de Kafka recibe el evento.

Carga el Trip "Viaje a la Luna".

Llama a trip.ReserveSeats(2).

El Trip actualiza AvailableSeats a 8.

Se guarda en la BBDD de Catálogo.

Resultado Final:

BBDD 1: Booking { ..., Status: Confirmed }.

BBDD 2: Trip { ..., AvailableSeats: 8 }.

Esto cumple tus requisitos: dos vertical slices claros, dos Bounded Contexts distintos (uno sobre transacciones y otro sobre inventario), cada uno con su Aggregate Root (Booking, Trip) y su lógica, y una comunicación asíncrona (Kafka) que demuestra la separación.

