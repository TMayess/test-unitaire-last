package com.example.exo14.service;

import com.example.exo14.exception.BookUnavailableException;
import com.example.exo14.exception.MemberSuspendedException;
import com.example.exo14.model.Reservation;
import com.example.exo14.repository.BookRepository;
import com.example.exo14.repository.MemberRepository;
import com.example.exo14.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public Reservation reserve(String memberId, String bookId) {
        throw new UnsupportedOperationException();
    }

    public List<Reservation> getReservationsForBook(String bookId) {
        throw new UnsupportedOperationException();
    }

    public void notifyNextReservation(String bookId) {
        throw new UnsupportedOperationException();
    }
}
