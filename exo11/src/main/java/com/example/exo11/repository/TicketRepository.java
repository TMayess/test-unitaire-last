package com.example.exo11.repository;

import com.example.exo11.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TicketRepository {

    public Ticket save(Ticket ticket) {
        return null; // stub
    }

    public Optional<Ticket> findById(String id) {
        return Optional.empty(); // stub
    }

    public List<Ticket> findAll() {
        return List.of(); // stub
    }

    public void deleteAll() {
        // stub
    }
}
