package com.example.exo13.integration;

import com.example.exo13.dto.AmountRequest;
import com.example.exo13.dto.CreateAccountRequest;
import com.example.exo13.dto.TransferRequest;
import com.example.exo13.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullAccountLifecycle() throws Exception {
        // Arrange
        CreateAccountRequest createRequest = new CreateAccountRequest();
        createRequest.setNumber("IT001");
        createRequest.setOwner("Integration User");

        // Création
        MvcResult createResult = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Account created = objectMapper.readValue(createResult.getResponse().getContentAsString(), Account.class);
        assertThat(created.getBalance()).isEqualTo(0.0);

        // Dépôt
        AmountRequest deposit = new AmountRequest(500.0);
        MvcResult depositResult = mockMvc.perform(post("/accounts/IT001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andReturn();

        Account afterDeposit = objectMapper.readValue(depositResult.getResponse().getContentAsString(), Account.class);
        assertThat(afterDeposit.getBalance()).isEqualTo(500.0);

        // Retrait
        AmountRequest withdraw = new AmountRequest(200.0);
        MvcResult withdrawResult = mockMvc.perform(post("/accounts/IT001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andReturn();

        Account afterWithdraw = objectMapper.readValue(withdrawResult.getResponse().getContentAsString(), Account.class);
        assertThat(afterWithdraw.getBalance()).isEqualTo(300.0);

        // Récupération
        mockMvc.perform(get("/accounts/IT001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("IT001"))
                .andExpect(jsonPath("$.balance").value(300.0));
    }

    @Test
    void transfer_fullCycleIntegration() throws Exception {
        // Arrange
        CreateAccountRequest src = new CreateAccountRequest();
        src.setNumber("IT010");
        src.setOwner("Source");

        CreateAccountRequest dst = new CreateAccountRequest();
        dst.setNumber("IT011");
        dst.setOwner("Destination");

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(src)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dst)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts/IT010/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AmountRequest(1000.0))))
                .andExpect(status().isOk());

        // Virement
        TransferRequest transfer = new TransferRequest("IT010", "IT011", 400.0);
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk());

        // Vérification
        mockMvc.perform(get("/accounts/IT010"))
                .andExpect(jsonPath("$.balance").value(600.0));

        mockMvc.perform(get("/accounts/IT011"))
                .andExpect(jsonPath("$.balance").value(400.0));
    }
}
