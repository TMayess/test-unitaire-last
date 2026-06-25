package com.example.exo12.controller;

import com.example.exo12.model.Room;
import com.example.exo12.service.RoomService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomService service;

    @Test
    void createRoom_shouldReturn201WithCreatedRoom() throws Exception {
        // Arrange
        Room room = new Room("1", "Salle A", 10);
        when(service.createRoom(any())).thenReturn(room);

        String body = objectMapper.writeValueAsString(Map.of("name", "Salle A", "capacity", 10));

        // Act & Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Salle A"))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    void createRoom_shouldReturn400WhenNameIsMissing() throws Exception {
        // Arrange
        String body = objectMapper.writeValueAsString(Map.of("capacity", 10));

        // Act & Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRoom_shouldReturn400WhenCapacityIsZero() throws Exception {
        // Arrange
        String body = objectMapper.writeValueAsString(Map.of("name", "Salle A", "capacity", 0));

        // Act & Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRooms_shouldReturn200WithList() throws Exception {
        // Arrange
        List<Room> rooms = List.of(
                new Room("1", "Salle A", 10),
                new Room("2", "Salle B", 5)
        );
        when(service.getAllRooms()).thenReturn(rooms);

        // Act & Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
