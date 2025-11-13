package com.inigo.arch.bookings.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingJpaRepository extends JpaRepository<BookingJpa, UUID> {}
