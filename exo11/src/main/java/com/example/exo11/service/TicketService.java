package com.example.exo11.service;

import com.example.exo11.dto.CreateTicketRequest;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket createTicket(CreateTicketRequest request) {
        return null; // stub
    }

    public Ticket getById(String id) {
        return null; // stub
    }

    public List<Ticket> getAll() {
        return null; // stub
    }

    public Ticket updateStatus(String id, TicketStatus newStatus) {
        return null; // stub
    }
}
