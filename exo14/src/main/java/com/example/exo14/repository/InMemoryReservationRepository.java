package com.example.exo14.repository;

import com.example.exo14.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    private final ConcurrentHashMap<String, Reservation> store = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        store.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public List<Reservation> findByBookId(String bookId) {
        return store.values().stream()
                .filter(r -> r.getBookId().equals(bookId))
                .sorted((a, b) -> a.getReservationDate().compareTo(b.getReservationDate()))
                .toList();
    }

    @Override
    public Optional<Reservation> findNextByBookId(String bookId) {
        return findByBookId(bookId).stream().findFirst();
    }

    @Override
    public void delete(String id) {
        store.remove(id);
    }
}
