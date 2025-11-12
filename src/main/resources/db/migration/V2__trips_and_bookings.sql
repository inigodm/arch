/**
 * V1: Creación de la tabla de Reservas (Bookings)
 */
CREATE TABLE bookings (
    -- ID del Agregado (PK)
                          id UUID PRIMARY KEY,

    -- ID del usuario (del contexto de usuarios)
                          user_id UUID NOT NULL,

    -- ID del viaje (referencia al agregado de Catálogo)
                          trip_id UUID NOT NULL,

    -- Datos propios de la reserva
                          number_of_passengers INT NOT NULL,

    -- Estado de la reserva (PENDING, CONFIRMED, CANCELLED)
                          status VARCHAR(50) NOT NULL,

    -- Constraint para asegurar lógica de negocio a nivel de BBDD
                          CONSTRAINT chk_positive_passengers CHECK (number_of_passengers > 0)
);

-- Índices para mejorar el rendimiento de las búsquedas
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_trip_id ON bookings(trip_id);

CREATE TABLE trips (
    -- ID del Agregado (PK)
                       id UUID PRIMARY KEY,

    -- Datos descriptivos
                       name VARCHAR(255) NOT NULL,
                       destination VARCHAR(255) NOT NULL,

    -- Lógica de inventario
                       total_seats INT NOT NULL,
                       available_seats INT NOT NULL,

    -- Constraints para asegurar lógica de negocio a nivel de BBDD
                       CONSTRAINT chk_positive_total_seats CHECK (total_seats > 0),
                       CONSTRAINT chk_valid_available_seats CHECK (available_seats >= 0 AND available_seats <= total_seats)
);

-- (Opcional) Índice si se busca mucho por destino
CREATE INDEX idx_trips_destination ON trips(destination);