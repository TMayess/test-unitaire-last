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
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Loan> findActiveByBookId(String bookId) {
        return store.values().stream()
                .filter(l -> l.getBookId().equals(bookId) && l.getReturnDate() == null)
                .findFirst();
    }

    @Override
    public Loan save(Loan loan) {
        store.put(loan.getId(), loan);
        return loan;
    }
}
