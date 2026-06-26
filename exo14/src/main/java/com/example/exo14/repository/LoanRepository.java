package com.example.exo14.repository;

import com.example.exo14.model.Loan;

import java.util.Optional;

public interface LoanRepository {
    Optional<Loan> findById(String id);
    Optional<Loan> findActiveByBookId(String bookId);
    Loan save(Loan loan);
}
