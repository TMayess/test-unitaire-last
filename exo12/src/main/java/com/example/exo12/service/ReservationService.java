package com.example.exo12.service;

import com.example.exo12.dto.CreateReservationRequest;
import com.example.exo12.exception.AlreadyCancelledException;
import com.example.exo12.exception.InvalidReservationTimeException;
import com.example.exo12.exception.ReservationConflictException;
import com.example.exo12.exception.ReservationNotFoundException;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Reservation createReservation(CreateReservationRequest request) {
        roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));

        if (!request.getEnd().isAfter(request.getStart())) {
            throw new InvalidReservationTimeException();
        }

        boolean hasConflict = reservationRepository.findByRoomId(request.getRoomId()).stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .anyMatch(r -> overlaps(r.getStart(), r.getEnd(), request.getStart(), request.getEnd()));

        if (hasConflict) {
            throw new ReservationConflictException(request.getRoomId());
        }

        Reservation reservation = new Reservation(
                UUID.randomUUID().toString(),
                request.getRoomId(),
                request.getReservedBy(),
                request.getStart(),
                request.getEnd(),
                ReservationStatus.CONFIRMED
        );
        return reservationRepository.save(reservation);
    }

    public Reservation getById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancelReservation(String id) {
        Reservation reservation = getById(id);
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new AlreadyCancelledException(id);
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    private boolean overlaps(LocalDateTime s1, LocalDateTime e1, LocalDateTime s2, LocalDateTime e2) {
        return s1.isBefore(e2) && s2.isBefore(e1);
    }
}
