package com.inigo.arch.bookings.infrastructure;

import com.inigo.arch.bookings.infrastructure.jpa.BookingJpa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @GetMapping("/{id}")
    public String getById(@PathVariable UUID id) {
        return "";
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody BookingJpa booking) {
        return ResponseEntity.ok().build();
    }
}
