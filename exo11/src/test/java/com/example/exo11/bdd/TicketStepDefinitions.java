package com.example.exo11.bdd;

import com.example.exo11.repository.TicketRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;




import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TicketStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository repository;

    private MvcResult lastResult;
    private String currentTicketId;

    @Before
    public void reset() {
        repository.deleteAll();
        currentTicketId = null;
        lastResult = null;
    }

    @Given("no ticket exists in the system")
    public void noTicketExists() {
        repository.deleteAll();
    }

    @Given("an open ticket with title {string} and priority {string}")
    public void anOpenTicket(String title, String priority) throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("title", title, "priority", priority)
        );
        MvcResult result = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        Map<?, ?> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        currentTicketId = (String) response.get("id");
    }

    @And("the ticket status has been changed to {string}")
    public void ticketStatusChangedTo(String status) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("status", status));
        mockMvc.perform(patch("/tickets/" + currentTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("I create a ticket with title {string} and priority {string}")
    public void iCreateATicket(String title, String priority) throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("title", title, "priority", priority)
        );
        lastResult = mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        if (lastResult.getResponse().getStatus() == 201) {
            Map<?, ?> response = objectMapper.readValue(lastResult.getResponse().getContentAsString(), Map.class);
            currentTicketId = (String) response.get("id");
        }
    }

    @When("I update the ticket status to {string}")
    public void iUpdateStatus(String status) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("status", status));
        lastResult = mockMvc.perform(patch("/tickets/" + currentTicketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
    }

    @When("I attempt to change the ticket status to {string}")
    public void iAttemptToChangeStatus(String status) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("status", status));
        lastResult = mockMvc.perform(patch("/tickets/" + currentTicketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
    }

    @When("I look up the ticket with id {string}")
    public void iLookUpTicket(String id) throws Exception {
        lastResult = mockMvc.perform(get("/tickets/" + id)).andReturn();
    }

    @Then("the ticket is created with status {string}")
    public void ticketCreatedWithStatus(String status) throws Exception {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(201);
        Map<?, ?> response = objectMapper.readValue(lastResult.getResponse().getContentAsString(), Map.class);
        assertThat(response.get("status")).isEqualTo(status);
    }

    @And("the ticket title is {string}")
    public void ticketTitleIs(String title) throws Exception {
        Map<?, ?> response = objectMapper.readValue(lastResult.getResponse().getContentAsString(), Map.class);
        assertThat(response.get("title")).isEqualTo(title);
    }

    @Then("the ticket status is {string}")
    public void ticketStatusIs(String status) throws Exception {
        Map<?, ?> response = objectMapper.readValue(lastResult.getResponse().getContentAsString(), Map.class);
        assertThat(response.get("status")).isEqualTo(status);
    }

    @Then("a conflict error is returned")
    public void conflictErrorReturned() {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(409);
    }

    @Then("a 404 error is returned")
    public void notFoundErrorReturned() {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(404);
    }
}