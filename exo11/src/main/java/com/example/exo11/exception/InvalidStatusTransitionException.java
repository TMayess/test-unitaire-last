package com.example.exo11.exception;

import com.example.exo11.model.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(TicketStatus from, TicketStatus to) {
        super("Transition interdite : " + from + " -> " + to);
    }
}
