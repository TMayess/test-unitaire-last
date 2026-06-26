package com.example.exo14.repository;

import com.example.exo14.model.Loan;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryLoanRepository implements LoanRepository {

    private final ConcurrentHashMap<String, Loan> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Loan> findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Loan> findActiveByBookId(String bookId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Loan save(Loan loan) {
        throw new UnsupportedOperationException();
    }
}
