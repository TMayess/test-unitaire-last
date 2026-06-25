package com.example.exo12.service;

import com.example.exo12.dto.CreateReservationRequest;
import com.example.exo12.model.Reservation;
import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Reservation createReservation(CreateReservationRequest request) { return null; }

    public Reservation getById(String id) { return null; }

    public Reservation cancelReservation(String id) { return null; }
}
