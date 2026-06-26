package com.example.exo13.repository;

import com.example.exo13.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    boolean existsByNumber(String number);
    Account save(Account account);
    Optional<Account> findByNumber(String number);
    List<Account> findAll();
}
