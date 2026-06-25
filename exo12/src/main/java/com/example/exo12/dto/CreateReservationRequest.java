package com.example.exo12.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateReservationRequest {

    @NotBlank(message = "L'identifiant de la salle est obligatoire")
    private String roomId;

    @NotBlank(message = "Le nom du réservant est obligatoire")
    private String reservedBy;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime start;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime end;

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getReservedBy() { return reservedBy; }
    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
}
