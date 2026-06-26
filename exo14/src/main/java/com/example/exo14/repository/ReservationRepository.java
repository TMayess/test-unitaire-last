package com.example.exo14.repository;

import com.example.exo14.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findByBookId(String bookId);
    Optional<Reservation> findNextByBookId(String bookId);
    void delete(String id);
}
