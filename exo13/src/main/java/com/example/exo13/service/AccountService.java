package com.example.exo13.service;

import com.example.exo13.dto.AmountRequest;
import com.example.exo13.dto.CreateAccountRequest;
import com.example.exo13.dto.TransferRequest;
import com.example.exo13.exception.AccountAlreadyExistsException;
import com.example.exo13.exception.AccountNotFoundException;
import com.example.exo13.exception.InsufficientFundsException;
import com.example.exo13.exception.InvalidAmountException;
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
        if (repository.existsByNumber(request.getNumber())) {
            throw new AccountAlreadyExistsException(request.getNumber());
        }
        return repository.save(new Account(request.getNumber(), request.getOwner(), 0.0));
    }

    public Account getByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public Account deposit(String number, AmountRequest request) {
        if (request.getAmount() <= 0) {
            throw new InvalidAmountException();
        }
        Account account = getByNumber(number);
        account.setBalance(account.getBalance() + request.getAmount());
        return repository.save(account);
    }

    public Account withdraw(String number, AmountRequest request) {
        if (request.getAmount() <= 0) {
            throw new InvalidAmountException();
        }
        Account account = getByNumber(number);
        if (account.getBalance() < request.getAmount()) {
            throw new InsufficientFundsException();
        }
        account.setBalance(account.getBalance() - request.getAmount());
        return repository.save(account);
    }

    public void transfer(TransferRequest request) {
        Account source = getByNumber(request.getSourceNumber());
        Account target = getByNumber(request.getTargetNumber());
        if (request.getAmount() <= 0) {
            throw new InvalidAmountException();
        }
        if (source.getBalance() < request.getAmount()) {
            throw new InsufficientFundsException();
        }
        source.setBalance(source.getBalance() - request.getAmount());
        target.setBalance(target.getBalance() + request.getAmount());
        repository.save(source);
        repository.save(target);
    }
}
