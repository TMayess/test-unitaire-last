package com.example.exo11.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String id) {
        super("Ticket introuvable : " + id);
    }
}
