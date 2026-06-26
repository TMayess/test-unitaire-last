package com.example.exo14.model;

import java.time.LocalDate;

public class Reservation {

    private String id;
    private String memberId;
    private String bookId;
    private LocalDate reservationDate;

    public Reservation() {}

    public Reservation(String id, String memberId, String bookId, LocalDate reservationDate) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
}
