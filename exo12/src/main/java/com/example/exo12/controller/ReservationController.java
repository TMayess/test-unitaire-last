package com.example.exo12.controller;

import com.example.exo12.dto.CreateReservationRequest;
import com.example.exo12.model.Reservation;
import com.example.exo12.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@Valid @RequestBody CreateReservationRequest request) { return null; }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable String id) { return null; }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable String id) { return null; }
}
