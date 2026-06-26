package com.example.exo13.repository;

import com.example.exo13.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentHashMap<String, Account> store = new ConcurrentHashMap<>();

    @Override
    public boolean existsByNumber(String number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Account save(Account account) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Account> findAll() {
        throw new UnsupportedOperationException();
    }
}
