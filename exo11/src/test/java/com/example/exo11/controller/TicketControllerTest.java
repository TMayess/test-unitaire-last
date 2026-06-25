package com.example.exo11.controller;

import com.example.exo11.exception.InvalidStatusTransitionException;
import com.example.exo11.exception.TicketNotFoundException;
import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TicketService service;

    // =====================
    // POST /tickets
    // =====================

    @Test
    void createTicket_shouldReturn201WithCreatedTicket() throws Exception {
        // Arrange
        Ticket ticket = new Ticket("1", "Problème réseau", Priority.HIGH, TicketStatus.OPEN);
        when(service.createTicket(any())).thenReturn(ticket);

        String body = objectMapper.writeValueAsString(
                Map.of("title", "Problème réseau", "priority", "HIGH")
        );

        // Act & Assert
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Problème réseau"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createTicket_shouldReturn400WhenTitleIsMissing() throws Exception {
        // Arrange
        String body = objectMapper.writeValueAsString(Map.of("priority", "HIGH"));

        // Act & Assert
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicket_shouldReturn400WhenTitleIsTooShort() throws Exception {
        // Arrange
        String body = objectMapper.writeValueAsString(Map.of("title", "AB", "priority", "HIGH"));

        // Act & Assert
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicket_shouldReturn400WhenPriorityIsMissing() throws Exception {
        // Arrange
        String body = objectMapper.writeValueAsString(Map.of("title", "Problème réseau"));

        // Act & Assert
        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // =====================
    // GET /tickets/{id}
    // =====================

    @Test
    void getById_shouldReturn200WithTicket() throws Exception {
        // Arrange
        Ticket ticket = new Ticket("1", "Bug login", Priority.MEDIUM, TicketStatus.OPEN);
        when(service.getById("1")).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Bug login"));
    }

    @Test
    void getById_shouldReturn404WhenTicketNotFound() throws Exception {
        // Arrange
        when(service.getById("999")).thenThrow(new TicketNotFoundException("999"));

        // Act & Assert
        mockMvc.perform(get("/tickets/999"))
                .andExpect(status().isNotFound());
    }

    // =====================
    // GET /tickets
    // =====================

    @Test
    void getAll_shouldReturn200WithListOfTickets() throws Exception {
        // Arrange
        List<Ticket> tickets = List.of(
                new Ticket("1", "Bug login", Priority.HIGH, TicketStatus.OPEN),
                new Ticket("2", "Lenteur", Priority.LOW, TicketStatus.IN_PROGRESS)
        );
        when(service.getAll()).thenReturn(tickets);

        // Act & Assert
        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ========================
    // PATCH /tickets/{id}/status
    // ========================

    @Test
    void updateStatus_shouldReturn200WithUpdatedTicket() throws Exception {
        // Arrange
        Ticket updated = new Ticket("1", "Bug", Priority.HIGH, TicketStatus.IN_PROGRESS);
        when(service.updateStatus(eq("1"), eq(TicketStatus.IN_PROGRESS))).thenReturn(updated);

        String body = objectMapper.writeValueAsString(Map.of("status", "IN_PROGRESS"));

        // Act & Assert
        mockMvc.perform(patch("/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateStatus_shouldReturn404WhenTicketNotFound() throws Exception {
        // Arrange
        when(service.updateStatus(eq("999"), any()))
                .thenThrow(new TicketNotFoundException("999"));

        String body = objectMapper.writeValueAsString(Map.of("status", "IN_PROGRESS"));

        // Act & Assert
        mockMvc.perform(patch("/tickets/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStatus_shouldReturn409WhenTransitionForbidden() throws Exception {
        // Arrange
        when(service.updateStatus(eq("1"), any()))
                .thenThrow(new InvalidStatusTransitionException(TicketStatus.RESOLVED, TicketStatus.OPEN));

        String body = objectMapper.writeValueAsString(Map.of("status", "OPEN"));

        // Act & Assert
        mockMvc.perform(patch("/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }
}
