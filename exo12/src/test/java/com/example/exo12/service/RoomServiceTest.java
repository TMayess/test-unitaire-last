package com.example.exo12.service;

import com.example.exo12.dto.CreateRoomRequest;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Room;
import com.example.exo12.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repository;

    @InjectMocks
    private RoomService service;

    private CreateRoomRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateRoomRequest();
        validRequest.setName("Salle A");
        validRequest.setCapacity(10);
    }

    @Test
    void createRoom_shouldReturnRoomWithCorrectNameAndCapacity() {
        // Arrange
        Room saved = new Room("1", "Salle A", 10);
        when(repository.save(any())).thenReturn(saved);

        // Act
        Room result = service.createRoom(validRequest);

        // Assert
        assertThat(result.getName()).isEqualTo("Salle A");
        assertThat(result.getCapacity()).isEqualTo(10);
    }

    @Test
    void createRoom_shouldCallRepositorySave() {
        // Arrange
        Room saved = new Room("1", "Salle A", 10);
        when(repository.save(any())).thenReturn(saved);

        // Act
        service.createRoom(validRequest);

        // Assert
        verify(repository, times(1)).save(any(Room.class));
    }

    @Test
    void getAllRooms_shouldReturnAllRooms() {
        // Arrange
        List<Room> rooms = List.of(
                new Room("1", "Salle A", 10),
                new Room("2", "Salle B", 5)
        );
        when(repository.findAll()).thenReturn(rooms);

        // Act
        List<Room> result = service.getAllRooms();

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void getById_shouldReturnRoomWhenExists() {
        // Arrange
        Room room = new Room("1", "Salle A", 10);
        when(repository.findById("1")).thenReturn(Optional.of(room));

        // Act
        Room result = service.getById("1");

        // Assert
        assertThat(result.getId()).isEqualTo("1");
    }

    @Test
    void getById_shouldThrowRoomNotFoundExceptionWhenNotExists() {
        // Arrange
        when(repository.findById("999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getById("999"))
                .isInstanceOf(RoomNotFoundException.class);
    }
}
