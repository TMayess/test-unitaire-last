package com.example.exo14.bdd;

import com.example.exo14.exception.BookUnavailableException;
import com.example.exo14.exception.MemberSuspendedException;
import com.example.exo14.model.Book;
import com.example.exo14.model.Member;
import com.example.exo14.model.Reservation;
import com.example.exo14.repository.BookRepository;
import com.example.exo14.repository.MemberRepository;
import com.example.exo14.service.ReservationService;
import io.cucumber.java.fr.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationStepDefinitions {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Exception caughtException;
    private boolean notified;

    @Soit("l'ouvrage {string} avec l'identifiant {string} est indisponible")
    public void ouvrage_indisponible(String title, String bookId) {
        Book book = new Book(bookId, title);
        book.setAvailable(false);
        bookRepository.save(book);
    }

    @Soit("l'ouvrage {string} avec l'identifiant {string} est disponible")
    public void ouvrage_disponible(String title, String bookId) {
        Book book = new Book(bookId, title);
        book.setAvailable(true);
        bookRepository.save(book);
    }

    @Soit("l'adhérent {string} avec l'identifiant {string} est actif")
    public void adherent_actif(String name, String memberId) {
        memberRepository.save(new Member(memberId, name));
    }

    @Soit("l'adhérent {string} avec l'identifiant {string} est suspendu")
    public void adherent_suspendu(String name, String memberId) {
        Member member = new Member(memberId, name);
        member.setSuspended(true);
        memberRepository.save(member);
    }

    @Soit("l'adhérent {string} a réservé l'ouvrage {string}")
    public void adherent_a_reserve(String memberId, String bookId) {
        reservationService.reserve(memberId, bookId);
    }

    @Quand("l'adhérent {string} réserve l'ouvrage {string}")
    public void adherent_reserve(String memberId, String bookId) {
        reservationService.reserve(memberId, bookId);
    }

    @Quand("l'adhérent {string} tente de réserver l'ouvrage {string}")
    public void adherent_tente_de_reserver(String memberId, String bookId) {
        try {
            reservationService.reserve(memberId, bookId);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Quand("l'ouvrage {string} est restitué")
    public void ouvrage_restitue(String bookId) {
        notified = false;
        reservationService.notifyNextReservation(bookId);
        notified = true;
    }

    @Alors("la réservation est enregistrée pour l'ouvrage {string}")
    public void reservation_enregistree(String bookId) {
        List<Reservation> reservations = reservationService.getReservationsForBook(bookId);
        assertThat(reservations).hasSize(1);
    }

    @Alors("il y a {int} réservations pour l'ouvrage {string}")
    public void nombre_reservations(int count, String bookId) {
        List<Reservation> reservations = reservationService.getReservationsForBook(bookId);
        assertThat(reservations).hasSize(count);
    }

    @Alors("la prochaine réservation de l'ouvrage {string} est notifiée")
    public void prochaine_reservation_notifiee(String bookId) {
        assertThat(notified).isTrue();
    }

    @Alors("la réservation est refusée pour cause de suspension")
    public void reservation_refusee_suspension() {
        assertThat(caughtException).isInstanceOf(MemberSuspendedException.class);
    }

    @Alors("la réservation est refusée car l'ouvrage est disponible")
    public void reservation_refusee_disponible() {
        assertThat(caughtException).isInstanceOf(BookUnavailableException.class);
    }
}
