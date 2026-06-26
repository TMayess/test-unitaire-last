package com.example.exo13.controller;

import com.example.exo13.dto.AmountRequest;
import com.example.exo13.dto.CreateAccountRequest;
import com.example.exo13.dto.TransferRequest;
import com.example.exo13.model.Account;
import com.example.exo13.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody CreateAccountRequest request) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{number}")
    public Account getOne(@PathVariable String number) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<Account> getAll() {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/{number}/deposit")
    public Account deposit(@PathVariable String number, @RequestBody AmountRequest request) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/{number}/withdraw")
    public Account withdraw(@PathVariable String number, @RequestBody AmountRequest request) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        throw new UnsupportedOperationException();
    }
}
