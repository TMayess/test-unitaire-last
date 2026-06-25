package com.example.exo12.bdd;

import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ReservationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private MvcResult lastResult;
    private String currentRoomId;

    @Before
    public void reset() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
        currentRoomId = null;
        lastResult = null;
    }

    @Given("no room exists in the system")
    public void noRoomExists() {
        roomRepository.deleteAll();
    }

    @Given("a room named {string} with capacity {int} exists")
    public void aRoomExists(String name, int capacity) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("name", name, "capacity", capacity));
        MvcResult result = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
        Map<?, ?> response = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        currentRoomId = (String) response.get("id");
    }

    @And("the room is reserved from {string} to {string} by {string}")
    public void theRoomIsReserved(String start, String end, String reservedBy) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", currentRoomId,
                "reservedBy", reservedBy,
                "start", start,
                "end", end
        ));
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("I reserve this room from {string} to {string} for {string}")
    public void iReserveThisRoom(String start, String end, String reservedBy) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", currentRoomId,
                "reservedBy", reservedBy,
                "start", start,
                "end", end
        ));
        lastResult = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
    }

    @When("I try to reserve room {string} from {string} to {string} for {string}")
    public void iTryToReserveRoom(String roomId, String start, String end, String reservedBy) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "roomId", roomId,
                "reservedBy", reservedBy,
                "start", start,
                "end", end
        ));
        lastResult = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
    }

    @Then("the reservation is confirmed")
    public void theReservationIsConfirmed() throws Exception {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(201);
        Map<?, ?> response = objectMapper.readValue(lastResult.getResponse().getContentAsString(), Map.class);
        assertThat(response.get("status")).isEqualTo("CONFIRMED");
    }

    @Then("a 404 error is returned")
    public void a404ErrorIsReturned() {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(404);
    }

    @Then("a conflict error is returned")
    public void aConflictErrorIsReturned() {
        assertThat(lastResult.getResponse().getStatus()).isEqualTo(409);
    }
}
