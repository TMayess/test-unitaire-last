package com.example.exo14.repository;

import com.example.exo14.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    @Override
    public Reservation save(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Reservation> findByBookId(String bookId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Reservation> findNextByBookId(String bookId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException();
    }
}
