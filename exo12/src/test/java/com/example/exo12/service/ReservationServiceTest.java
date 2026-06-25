package com.example.exo12.service;

import com.example.exo12.dto.CreateReservationRequest;
import com.example.exo12.exception.AlreadyCancelledException;
import com.example.exo12.exception.InvalidReservationTimeException;
import com.example.exo12.exception.ReservationConflictException;
import com.example.exo12.exception.ReservationNotFoundException;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Reservation;
import com.example.exo12.model.ReservationStatus;
import com.example.exo12.model.Room;
import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService service;

    private final LocalDateTime start = LocalDateTime.of(2026, 7, 1, 9, 0);
    private final LocalDateTime end = LocalDateTime.of(2026, 7, 1, 10, 0);
    private Room room;
    private CreateReservationRequest validRequest;

    @BeforeEach
    void setUp() {
        room = new Room("room-1", "Salle A", 10);
        validRequest = new CreateReservationRequest();
        validRequest.setRoomId("room-1");
        validRequest.setReservedBy("Alice");
        validRequest.setStart(start);
        validRequest.setEnd(end);
    }

    @Test
    void createReservation_shouldReturnConfirmedReservation() {
        // Arrange
        when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId("room-1")).thenReturn(List.of());
        Reservation saved = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CONFIRMED);
        when(reservationRepository.save(any())).thenReturn(saved);

        // Act
        Reservation result = service.createReservation(validRequest);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(result.getReservedBy()).isEqualTo("Alice");
    }

    @Test
    void createReservation_shouldThrowRoomNotFoundExceptionWhenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findById("room-1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.createReservation(validRequest))
                .isInstanceOf(RoomNotFoundException.class);
    }

    @Test
    void createReservation_shouldThrowInvalidReservationTimeExceptionWhenEndBeforeStart() {
        // Arrange
        validRequest.setEnd(start.minusHours(1));
        when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));

        // Act & Assert
        assertThatThrownBy(() -> service.createReservation(validRequest))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    void createReservation_shouldThrowInvalidReservationTimeExceptionWhenEndEqualsStart() {
        // Arrange
        validRequest.setEnd(start);
        when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));

        // Act & Assert
        assertThatThrownBy(() -> service.createReservation(validRequest))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    void createReservation_shouldThrowConflictWhenOverlapsExistingConfirmedReservation() {
        // Arrange
        Reservation existing = new Reservation("r0", "room-1", "Bob",
                start.minusMinutes(30), end.minusMinutes(30), ReservationStatus.CONFIRMED);
        when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId("room-1")).thenReturn(List.of(existing));

        // Act & Assert
        assertThatThrownBy(() -> service.createReservation(validRequest))
                .isInstanceOf(ReservationConflictException.class);
    }

    @Test
    void createReservation_shouldAllowWhenOverlapIsWithCancelledReservation() {
        // Arrange
        Reservation cancelled = new Reservation("r0", "room-1", "Bob",
                start.minusMinutes(30), end.minusMinutes(30), ReservationStatus.CANCELLED);
        when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId("room-1")).thenReturn(List.of(cancelled));
        Reservation saved = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CONFIRMED);
        when(reservationRepository.save(any())).thenReturn(saved);

        // Act
        Reservation result = service.createReservation(validRequest);

        // Assert
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    void cancelReservation_shouldSetStatusToCancelled() {
        // Arrange
        Reservation reservation = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CONFIRMED);
        when(reservationRepository.findById("r1")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation result = service.cancelReservation("r1");

        // Assert
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void cancelReservation_shouldThrowAlreadyCancelledExceptionWhenAlreadyCancelled() {
        // Arrange
        Reservation reservation = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CANCELLED);
        when(reservationRepository.findById("r1")).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThatThrownBy(() -> service.cancelReservation("r1"))
                .isInstanceOf(AlreadyCancelledException.class);
    }

    @Test
    void cancelReservation_shouldThrowReservationNotFoundExceptionWhenNotExists() {
        // Arrange
        when(reservationRepository.findById("r999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.cancelReservation("r999"))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void getById_shouldReturnReservationWhenExists() {
        // Arrange
        Reservation reservation = new Reservation("r1", "room-1", "Alice", start, end, ReservationStatus.CONFIRMED);
        when(reservationRepository.findById("r1")).thenReturn(Optional.of(reservation));

        // Act
        Reservation result = service.getById("r1");

        // Assert
        assertThat(result.getId()).isEqualTo("r1");
    }

    @Test
    void getById_shouldThrowReservationNotFoundExceptionWhenNotExists() {
        // Arrange
        when(reservationRepository.findById("r999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getById("r999"))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
