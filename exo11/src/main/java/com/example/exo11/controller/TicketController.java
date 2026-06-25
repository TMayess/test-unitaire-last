package com.example.exo11.controller;

import com.example.exo11.dto.CreateTicketRequest;
import com.example.exo11.dto.UpdateStatusRequest;
import com.example.exo11.model.Ticket;
import com.example.exo11.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Ticket> create(@Valid @RequestBody CreateTicketRequest request) {
        return null; // stub
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable String id) {
        return null; // stub
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAll() {
        return null; // stub
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable String id,
                                               @Valid @RequestBody UpdateStatusRequest request) {
        return null; // stub
    }
}
