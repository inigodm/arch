package com.inigo.arch.bookings.infrastructure.jpa;

import jakarta.persistence.*;

import java.util.UUID;
import org.springframework.data.domain.AbstractAggregateRoot;

/**
 * Aggregate Root para el Bounded Context de Reservas.
 * Usamos AbstractAggregateRoot de Spring Data para facilitar la publicación
 * de eventos de dominio.
 */
@Entity
@Table(name = "bookings")
public class BookingJpa extends AbstractAggregateRoot<BookingJpa> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId; // ID del usuario del contexto de Usuarios

    @Column(nullable = false)
    private UUID tripId; // ID del viaje (del contexto de Catálogo)

    @Column(nullable = false)
    private int numberOfPassengers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getTripId() {
        return tripId;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public BookingStatus getStatus() {
        return status;
    }
}

