package com.example.exo12.integration;

import com.example.exo12.model.Reservation;
import com.example.exo12.model.Room;
import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void fullLifecycle_createRoomThenReserveThenConsultThenCancel() throws Exception {
        String roomBody = objectMapper.writeValueAsString(Map.of("name", "Salle Intégration", "capacity", 8));
        MvcResult roomResult = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomBody))
                .andExpect(status().isCreated())
                .andReturn();

        Room room = objectMapper.readValue(roomResult.getResponse().getContentAsString(), Room.class);
        assertThat(room.getId()).isNotNull();

        String reservationBody = objectMapper.writeValueAsString(Map.of(
                "roomId", room.getId(),
                "reservedBy", "Alice",
                "start", "2026-08-01T09:00:00",
                "end", "2026-08-01T10:00:00"
        ));
        MvcResult reservationResult = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andReturn();

        Reservation reservation = objectMapper.readValue(reservationResult.getResponse().getContentAsString(), Reservation.class);
        String reservationId = reservation.getId();
        assertThat(reservationId).isNotNull();

        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservedBy").value("Alice"));

        mockMvc.perform(patch("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
