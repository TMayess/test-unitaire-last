package com.example.exo13.service;

import com.example.exo13.dto.AmountRequest;
import com.example.exo13.dto.CreateAccountRequest;
import com.example.exo13.dto.TransferRequest;
import com.example.exo13.model.Account;
import com.example.exo13.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(CreateAccountRequest request) {
        throw new UnsupportedOperationException();
    }

    public Account getByNumber(String number) {
        throw new UnsupportedOperationException();
    }

    public List<Account> getAllAccounts() {
        throw new UnsupportedOperationException();
    }

    public Account deposit(String number, AmountRequest request) {
        throw new UnsupportedOperationException();
    }

    public Account withdraw(String number, AmountRequest request) {
        throw new UnsupportedOperationException();
    }

    public void transfer(TransferRequest request) {
        throw new UnsupportedOperationException();
    }
}
