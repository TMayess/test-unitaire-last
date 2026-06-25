package com.example.exo11.integration;

import com.example.exo11.dto.CreateTicketRequest;
import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class TicketIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullTicketLifecycle_createThenGetThenUpdateStatus() throws Exception {
        // Étape 1 : Créer un ticket
        String createBody = objectMapper.writeValueAsString(
                Map.of("title", "Problème réseau critique", "priority", "HIGH")
        );

        MvcResult createResult = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn();

        Ticket created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Ticket.class);
        String ticketId = created.getId();
        assertThat(ticketId).isNotNull();

        // Étape 2 : Récupérer ce ticket
        mockMvc.perform(get("/tickets/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.title").value("Problème réseau critique"));

        // Étape 3 : Modifier le statut
        String updateBody = objectMapper.writeValueAsString(Map.of("status", "IN_PROGRESS"));

        mockMvc.perform(patch("/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
}
