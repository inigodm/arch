package com.inigo.arch.trip.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripJpaRepository extends JpaRepository<TripJpa, UUID> {
}
