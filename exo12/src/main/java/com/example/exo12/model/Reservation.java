package com.example.exo12.model;

import java.time.LocalDateTime;

public class Reservation {
    private String id;
    private String roomId;
    private String reservedBy;
    private LocalDateTime start;
    private LocalDateTime end;
    private ReservationStatus status;

    public Reservation() {}

    public Reservation(String id, String roomId, String reservedBy,
                       LocalDateTime start, LocalDateTime end, ReservationStatus status) {
        this.id = id;
        this.roomId = roomId;
        this.reservedBy = reservedBy;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getReservedBy() { return reservedBy; }
    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
