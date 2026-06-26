package com.example.exo14.exception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(String bookId) {
        super("Book unavailable: " + bookId);
    }
}
