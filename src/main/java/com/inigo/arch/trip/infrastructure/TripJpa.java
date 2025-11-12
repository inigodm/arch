package com.inigo.arch.trip.infrastructure;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Aggregate Root para el Bounded Context de Catálogo.
 * Representa un viaje y su inventario de plazas.
 */
@Entity
@Table(name = "trips")
public class TripJpa {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private int totalSeats; // Capacidad total

    @Column(nullable = false)
    private int availableSeats; // Plazas disponibles

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}