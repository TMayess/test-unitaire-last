package com.example.exo13.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String number) {
        super("Account not found: " + number);
    }
}
