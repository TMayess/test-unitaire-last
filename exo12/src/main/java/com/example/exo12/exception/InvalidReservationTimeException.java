package com.example.exo12.exception;

public class InvalidReservationTimeException extends RuntimeException {
    public InvalidReservationTimeException() {
        super("La date de fin doit être strictement après la date de début");
    }
}
