package com.example.exo14.service;

import com.example.exo14.exception.BookUnavailableException;
import com.example.exo14.exception.MemberSuspendedException;
import com.example.exo14.model.Book;
import com.example.exo14.model.Member;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        if (member.isSuspended()) {
            throw new MemberSuspendedException(memberId);
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        if (book.isAvailable()) {
            throw new BookUnavailableException(bookId);
        }
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), memberId, bookId, LocalDate.now());
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsForBook(String bookId) {
        return reservationRepository.findByBookId(bookId);
    }

    public void notifyNextReservation(String bookId) {
        reservationRepository.findNextByBookId(bookId)
                .ifPresent(r -> reservationRepository.delete(r.getId()));
    }
}
