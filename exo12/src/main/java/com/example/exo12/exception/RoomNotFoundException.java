package com.example.exo12.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(String id) {
        super("Salle introuvable : " + id);
    }
}
