package com.example.exo12.controller;

import com.example.exo12.exception.AlreadyCancelledException;
import com.example.exo12.exception.ReservationConflictException;
import com.example.exo12.exception.ReservationNotFoundException;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
import com.example.exo12.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService service;

    private final LocalDateTime start = LocalDateTime.of(2026, 7, 1, 9, 0);
    private final LocalDateTime end = LocalDateTime.of(2026, 7, 1, 10, 0);

    @Test
    void createReservation_shouldReturn201WithConfirmedReservation() throws Exception {
        // Arrange
        Reservation reservation = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CONFIRMED);
        when(service.createReservation(any())).thenReturn(reservation);

        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", "room-1",
                "reservedBy", "Alice",
                "start", "2026-07-01T09:00:00",
                "end", "2026-07-01T10:00:00"
        ));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("r1"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void createReservation_shouldReturn400WhenReservedByIsMissing() throws Exception {
        // Arrange
        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", "room-1",
                "start", "2026-07-01T09:00:00",
                "end", "2026-07-01T10:00:00"
        ));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservation_shouldReturn404WhenRoomDoesNotExist() throws Exception {
        // Arrange
        when(service.createReservation(any())).thenThrow(new RoomNotFoundException("room-1"));

        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", "room-1",
                "reservedBy", "Alice",
                "start", "2026-07-01T09:00:00",
                "end", "2026-07-01T10:00:00"
        ));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void createReservation_shouldReturn409WhenSlotConflicts() throws Exception {
        // Arrange
        when(service.createReservation(any())).thenThrow(new ReservationConflictException("room-1"));

        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", "room-1",
                "reservedBy", "Alice",
                "start", "2026-07-01T09:00:00",
                "end", "2026-07-01T10:00:00"
        ));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void getById_shouldReturn200WithReservation() throws Exception {
        // Arrange
        Reservation reservation = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CONFIRMED);
        when(service.getById("r1")).thenReturn(reservation);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/r1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("r1"))
                .andExpect(jsonPath("$.reservedBy").value("Alice"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        when(service.getById("r999")).thenThrow(new ReservationNotFoundException("r999"));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/r999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelReservation_shouldReturn200WithCancelledReservation() throws Exception {
        // Arrange
        Reservation cancelled = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CANCELLED);
        when(service.cancelReservation(eq("r1"))).thenReturn(cancelled);

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/r1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancelReservation_shouldReturn409WhenAlreadyCancelled() throws Exception {
        // Arrange
        when(service.cancelReservation(eq("r1"))).thenThrow(new AlreadyCancelledException("r1"));

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/r1/cancel"))
                .andExpect(status().isConflict());
    }
}
