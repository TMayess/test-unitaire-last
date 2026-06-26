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
        return store.containsKey(number);
    }

    @Override
    public Account save(Account account) {
        store.put(account.getNumber(), account);
        return account;
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        return Optional.ofNullable(store.get(number));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }
}
