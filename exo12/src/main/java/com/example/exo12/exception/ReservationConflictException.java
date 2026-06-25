package com.example.exo12.exception;

public class ReservationConflictException extends RuntimeException {
    public ReservationConflictException(String roomId) {
        super("Créneau déjà réservé pour la salle : " + roomId);
    }
}
