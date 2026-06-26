package com.example.exo14.repository;

import com.example.exo14.model.Book;

import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(String id);
    Book save(Book book);
}
