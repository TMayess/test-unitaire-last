package com.example.exo11.service;

import com.example.exo11.dto.CreateTicketRequest;
import com.example.exo11.exception.InvalidStatusTransitionException;
import com.example.exo11.exception.TicketNotFoundException;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getPriority(),
                TicketStatus.OPEN
        );
        return repository.save(ticket);
    }

    public Ticket getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> getAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(String id, TicketStatus newStatus) {
        Ticket ticket = getById(id);
        validateTransition(ticket.getStatus(), newStatus);
        ticket.setStatus(newStatus);
        return repository.save(ticket);
    }

    private void validateTransition(TicketStatus current, TicketStatus next) {
        boolean allowed = switch (current) {
            case OPEN -> next == TicketStatus.IN_PROGRESS || next == TicketStatus.RESOLVED;
            case IN_PROGRESS -> next == TicketStatus.RESOLVED;
            case RESOLVED -> false;
        };
        if (!allowed) {
            throw new InvalidStatusTransitionException(current, next);
        }
    }
}
