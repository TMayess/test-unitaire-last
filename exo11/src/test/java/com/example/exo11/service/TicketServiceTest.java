package com.example.exo11.service;

import com.example.exo11.dto.CreateTicketRequest;
import com.example.exo11.exception.InvalidStatusTransitionException;
import com.example.exo11.exception.TicketNotFoundException;
import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.repository.TicketRepository;
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
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    private CreateTicketRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateTicketRequest();
        validRequest.setTitle("Problème réseau");
        validRequest.setPriority(Priority.HIGH);
    }

    // =====================
    // Création d'un ticket
    // =====================

    @Test
    void createTicket_shouldReturnTicketWithCorrectTitleAndPriority() {
        // Arrange
        Ticket saved = new Ticket("1", "Problème réseau", Priority.HIGH, TicketStatus.OPEN);
        when(repository.save(any())).thenReturn(saved);

        // Act
        Ticket result = service.createTicket(validRequest);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Problème réseau");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void createTicket_shouldAlwaysSetStatusToOpen() {
        // Arrange
        Ticket saved = new Ticket("1", "Problème réseau", Priority.HIGH, TicketStatus.OPEN);
        when(repository.save(any())).thenReturn(saved);

        // Act
        Ticket result = service.createTicket(validRequest);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void createTicket_shouldCallRepositorySave() {
        // Arrange
        Ticket saved = new Ticket("1", "Problème réseau", Priority.HIGH, TicketStatus.OPEN);
        when(repository.save(any())).thenReturn(saved);

        // Act
        service.createTicket(validRequest);

        // Assert
        verify(repository, times(1)).save(any(Ticket.class));
    }

    // =====================
    // Recherche d'un ticket
    // =====================

    @Test
    void getById_shouldReturnTicketWhenExists() {
        // Arrange
        Ticket ticket = new Ticket("1", "Problème réseau", Priority.HIGH, TicketStatus.OPEN);
        when(repository.findById("1")).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.getById("1");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    void getById_shouldThrowTicketNotFoundExceptionWhenNotExists() {
        // Arrange
        when(repository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getById("999"))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void getAll_shouldReturnAllTickets() {
        // Arrange
        List<Ticket> tickets = List.of(
                new Ticket("1", "Bug login", Priority.HIGH, TicketStatus.OPEN),
                new Ticket("2", "Lenteur serveur", Priority.LOW, TicketStatus.IN_PROGRESS)
        );
        when(repository.findAll()).thenReturn(tickets);

        // Act
        List<Ticket> result = service.getAll();

        // Assert
        assertThat(result).hasSize(2);
    }

    // ========================
    // Transitions de statut
    // ========================

    @Test
    void updateStatus_shouldAllowOpenToInProgress() {
        // Arrange
        Ticket ticket = new Ticket("1", "Bug", Priority.MEDIUM, TicketStatus.OPEN);
        when(repository.findById("1")).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.updateStatus("1", TicketStatus.IN_PROGRESS);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);
    }

    @Test
    void updateStatus_shouldAllowOpenToResolved() {
        // Arrange
        Ticket ticket = new Ticket("1", "Bug", Priority.MEDIUM, TicketStatus.OPEN);
        when(repository.findById("1")).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.updateStatus("1", TicketStatus.RESOLVED);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.RESOLVED);
    }

    @Test
    void updateStatus_shouldAllowInProgressToResolved() {
        // Arrange
        Ticket ticket = new Ticket("1", "Bug", Priority.MEDIUM, TicketStatus.IN_PROGRESS);
        when(repository.findById("1")).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Ticket result = service.updateStatus("1", TicketStatus.RESOLVED);

        // Assert
        assertThat(result.getStatus()).isEqualTo(TicketStatus.RESOLVED);
    }

    @Test
    void updateStatus_shouldRejectTransitionFromResolvedToAnyStatus() {
        // Arrange
        Ticket ticket = new Ticket("1", "Bug", Priority.MEDIUM, TicketStatus.RESOLVED);
        when(repository.findById("1")).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus("1", TicketStatus.OPEN))
                .isInstanceOf(InvalidStatusTransitionException.class);

        assertThatThrownBy(() -> service.updateStatus("1", TicketStatus.IN_PROGRESS))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void updateStatus_shouldRejectTransitionFromInProgressToOpen() {
        // Arrange
        Ticket ticket = new Ticket("1", "Bug", Priority.MEDIUM, TicketStatus.IN_PROGRESS);
        when(repository.findById("1")).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus("1", TicketStatus.OPEN))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void updateStatus_shouldThrowTicketNotFoundExceptionWhenTicketDoesNotExist() {
        // Arrange
        when(repository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.updateStatus("999", TicketStatus.IN_PROGRESS))
                .isInstanceOf(TicketNotFoundException.class);
    }
}
