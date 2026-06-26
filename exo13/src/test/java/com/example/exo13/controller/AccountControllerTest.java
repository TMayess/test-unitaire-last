package com.example.exo13.controller;

import com.example.exo13.dto.AmountRequest;
import com.example.exo13.dto.CreateAccountRequest;
import com.example.exo13.dto.TransferRequest;
import com.example.exo13.exception.AccountAlreadyExistsException;
import com.example.exo13.exception.AccountNotFoundException;
import com.example.exo13.exception.InsufficientFundsException;
import com.example.exo13.exception.InvalidAmountException;
import com.example.exo13.model.Account;
import com.example.exo13.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @Test
    void createAccount_shouldReturn201WithAccount() throws Exception {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber("FR001");
        request.setOwner("Alice");
        Account account = new Account("FR001", "Alice", 0.0);
        when(accountService.createAccount(any())).thenReturn(account);

        // Act & Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("FR001"))
                .andExpect(jsonPath("$.owner").value("Alice"))
                .andExpect(jsonPath("$.balance").value(0.0));
    }

    @Test
    void createAccount_shouldReturn409WhenNumberAlreadyExists() throws Exception {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber("FR001");
        request.setOwner("Alice");
        when(accountService.createAccount(any())).thenThrow(new AccountAlreadyExistsException("FR001"));

        // Act & Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getAccount_shouldReturn200WithAccount() throws Exception {
        // Arrange
        Account account = new Account("FR001", "Alice", 100.0);
        when(accountService.getByNumber("FR001")).thenReturn(account);

        // Act & Assert
        mockMvc.perform(get("/accounts/FR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("FR001"));
    }

    @Test
    void getAccount_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        when(accountService.getByNumber("FR999")).thenThrow(new AccountNotFoundException("FR999"));

        // Act & Assert
        mockMvc.perform(get("/accounts/FR999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAccounts_shouldReturn200WithList() throws Exception {
        // Arrange
        List<Account> accounts = List.of(
                new Account("FR001", "Alice", 0.0),
                new Account("FR002", "Bob", 500.0)
        );
        when(accountService.getAllAccounts()).thenReturn(accounts);

        // Act & Assert
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deposit_shouldReturn200WithUpdatedAccount() throws Exception {
        // Arrange
        AmountRequest request = new AmountRequest(200.0);
        Account account = new Account("FR001", "Alice", 300.0);
        when(accountService.deposit(eq("FR001"), any())).thenReturn(account);

        // Act & Assert
        mockMvc.perform(post("/accounts/FR001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(300.0));
    }

    @Test
    void deposit_shouldReturn400WhenAmountInvalid() throws Exception {
        // Arrange
        AmountRequest request = new AmountRequest(-10.0);
        when(accountService.deposit(eq("FR001"), any())).thenThrow(new InvalidAmountException());

        // Act & Assert
        mockMvc.perform(post("/accounts/FR001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_shouldReturn200WithUpdatedAccount() throws Exception {
        // Arrange
        AmountRequest request = new AmountRequest(50.0);
        Account account = new Account("FR001", "Alice", 150.0);
        when(accountService.withdraw(eq("FR001"), any())).thenReturn(account);

        // Act & Assert
        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.0));
    }

    @Test
    void withdraw_shouldReturn400WhenAmountInvalid() throws Exception {
        // Arrange
        AmountRequest request = new AmountRequest(0.0);
        when(accountService.withdraw(eq("FR001"), any())).thenThrow(new InvalidAmountException());

        // Act & Assert
        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdraw_shouldReturn422WhenInsufficientFunds() throws Exception {
        // Arrange
        AmountRequest request = new AmountRequest(9000.0);
        when(accountService.withdraw(eq("FR001"), any())).thenThrow(new InsufficientFundsException());

        // Act & Assert
        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void transfer_shouldReturn200() throws Exception {
        // Arrange
        TransferRequest request = new TransferRequest("FR001", "FR002", 100.0);

        // Act & Assert
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void transfer_shouldReturn400WhenAmountInvalid() throws Exception {
        // Arrange
        TransferRequest request = new TransferRequest("FR001", "FR002", -50.0);
        doThrow(new InvalidAmountException()).when(accountService).transfer(any());

        // Act & Assert
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldReturn422WhenInsufficientFunds() throws Exception {
        // Arrange
        TransferRequest request = new TransferRequest("FR001", "FR002", 9000.0);
        doThrow(new InsufficientFundsException()).when(accountService).transfer(any());

        // Act & Assert
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void transfer_shouldReturn404WhenAccountNotFound() throws Exception {
        // Arrange
        TransferRequest request = new TransferRequest("FR999", "FR002", 100.0);
        doThrow(new AccountNotFoundException("FR999")).when(accountService).transfer(any());

        // Act & Assert
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
