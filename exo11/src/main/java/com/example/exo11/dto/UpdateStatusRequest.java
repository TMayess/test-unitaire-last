package com.example.exo11.dto;

import com.example.exo11.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "Le statut est obligatoire")
    private TicketStatus status;

    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
}
