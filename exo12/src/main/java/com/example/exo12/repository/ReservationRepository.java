package com.example.exo12.repository;

import com.example.exo12.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    public Reservation save(Reservation reservation) { return null; }

    public Optional<Reservation> findById(String id) { return Optional.empty(); }

    public List<Reservation> findByRoomId(String roomId) { return List.of(); }

    public void deleteAll() {}
}
