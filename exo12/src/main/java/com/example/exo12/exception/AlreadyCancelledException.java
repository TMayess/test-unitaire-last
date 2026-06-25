package com.example.exo12.exception;

public class AlreadyCancelledException extends RuntimeException {
    public AlreadyCancelledException(String id) {
        super("La réservation est déjà annulée : " + id);
    }
}
