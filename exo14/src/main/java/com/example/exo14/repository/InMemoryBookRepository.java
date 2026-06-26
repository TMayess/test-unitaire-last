package com.example.exo14.repository;

import com.example.exo14.model.Book;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookRepository implements BookRepository {

    private final ConcurrentHashMap<String, Book> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Book> findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Book save(Book book) {
        throw new UnsupportedOperationException();
    }
}
