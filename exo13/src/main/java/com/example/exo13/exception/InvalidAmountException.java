package com.example.exo13.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Invalid amount");
    }
}
