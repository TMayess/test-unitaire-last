package com.example.exo12.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String id) {
        super("Réservation introuvable : " + id);
    }
}
