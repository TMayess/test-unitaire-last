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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    private CreateAccountRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateAccountRequest();
        validRequest.setNumber("FR001");
        validRequest.setOwner("Alice");
    }

    @Test
    void createAccount_shouldReturnAccountWithZeroBalance() {
        // Arrange
        when(repository.existsByNumber("FR001")).thenReturn(false);
        when(repository.save(any())).thenReturn(new Account("FR001", "Alice", 0.0));

        // Act
        Account result = service.createAccount(validRequest);

        // Assert
        assertThat(result.getBalance()).isEqualTo(0.0);
        assertThat(result.getOwner()).isEqualTo("Alice");
        assertThat(result.getNumber()).isEqualTo("FR001");
    }

    @Test
    void createAccount_shouldThrowWhenNumberAlreadyExists() {
        // Arrange
        when(repository.existsByNumber("FR001")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> service.createAccount(validRequest))
                .isInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    void getByNumber_shouldReturnAccountWhenExists() {
        // Arrange
        Account account = new Account("FR001", "Alice", 0.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));

        // Act
        Account result = service.getByNumber("FR001");

        // Assert
        assertThat(result.getNumber()).isEqualTo("FR001");
    }

    @Test
    void getByNumber_shouldThrowWhenAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getByNumber("FR999"))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getAllAccounts_shouldReturnAllAccounts() {
        // Arrange
        List<Account> accounts = List.of(
                new Account("FR001", "Alice", 0.0),
                new Account("FR002", "Bob", 500.0)
        );
        when(repository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = service.getAllAccounts();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        // Arrange
        Account account = new Account("FR001", "Alice", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AmountRequest request = new AmountRequest(50.0);

        // Act
        Account result = service.deposit("FR001", request);

        // Assert
        assertThat(result.getBalance()).isEqualTo(150.0);
    }

    @Test
    void deposit_shouldThrowWhenAmountIsZero() {
        // Arrange
        Account account = new Account("FR001", "Alice", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        AmountRequest request = new AmountRequest(0.0);

        // Act & Assert
        assertThatThrownBy(() -> service.deposit("FR001", request))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void deposit_shouldThrowWhenAmountIsNegative() {
        // Arrange
        Account account = new Account("FR001", "Alice", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        AmountRequest request = new AmountRequest(-10.0);

        // Act & Assert
        assertThatThrownBy(() -> service.deposit("FR001", request))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        // Arrange
        Account account = new Account("FR001", "Alice", 200.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AmountRequest request = new AmountRequest(50.0);

        // Act
        Account result = service.withdraw("FR001", request);

        // Assert
        assertThat(result.getBalance()).isEqualTo(150.0);
    }

    @Test
    void withdraw_shouldThrowWhenAmountIsZero() {
        // Arrange
        Account account = new Account("FR001", "Alice", 200.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        AmountRequest request = new AmountRequest(0.0);

        // Act & Assert
        assertThatThrownBy(() -> service.withdraw("FR001", request))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void withdraw_shouldThrowWhenAmountIsNegative() {
        // Arrange
        Account account = new Account("FR001", "Alice", 200.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        AmountRequest request = new AmountRequest(-20.0);

        // Act & Assert
        assertThatThrownBy(() -> service.withdraw("FR001", request))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void withdraw_shouldThrowWhenBalanceInsufficient() {
        // Arrange
        Account account = new Account("FR001", "Alice", 30.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        AmountRequest request = new AmountRequest(100.0);

        // Act & Assert
        assertThatThrownBy(() -> service.withdraw("FR001", request))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {
        // Arrange
        Account source = new Account("FR001", "Alice", 500.0);
        Account target = new Account("FR002", "Bob", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(target));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        TransferRequest request = new TransferRequest("FR001", "FR002", 200.0);

        // Act
        service.transfer(request);

        // Assert
        assertThat(source.getBalance()).isEqualTo(300.0);
        assertThat(target.getBalance()).isEqualTo(300.0);
    }

    @Test
    void transfer_shouldThrowWhenAmountIsZero() {
        // Arrange
        Account source = new Account("FR001", "Alice", 500.0);
        Account target = new Account("FR002", "Bob", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(target));
        TransferRequest request = new TransferRequest("FR001", "FR002", 0.0);

        // Act & Assert
        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void transfer_shouldThrowWhenAmountIsNegative() {
        // Arrange
        Account source = new Account("FR001", "Alice", 500.0);
        Account target = new Account("FR002", "Bob", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(target));
        TransferRequest request = new TransferRequest("FR001", "FR002", -50.0);

        // Act & Assert
        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void transfer_shouldThrowWhenSourceBalanceInsufficient() {
        // Arrange
        Account source = new Account("FR001", "Alice", 50.0);
        Account target = new Account("FR002", "Bob", 100.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(target));
        TransferRequest request = new TransferRequest("FR001", "FR002", 200.0);

        // Act & Assert
        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void transfer_shouldThrowWhenSourceAccountDoesNotExist() {
        // Arrange
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());
        TransferRequest request = new TransferRequest("FR999", "FR002", 100.0);

        // Act & Assert
        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void transfer_shouldThrowWhenTargetAccountDoesNotExist() {
        // Arrange
        Account source = new Account("FR001", "Alice", 500.0);
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(source));
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());
        TransferRequest request = new TransferRequest("FR001", "FR999", 100.0);

        // Act & Assert
        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(AccountNotFoundException.class);
    }
}
