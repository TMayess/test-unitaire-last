package com.example.exo12.service;

import com.example.exo12.dto.CreateRoomRequest;
import com.example.exo12.exception.RoomNotFoundException;
import com.example.exo12.model.Room;
import com.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room createRoom(CreateRoomRequest request) {
        Room room = new Room(UUID.randomUUID().toString(), request.getName(), request.getCapacity());
        return repository.save(room);
    }

    public List<Room> getAllRooms() {
        return repository.findAll();
    }

    public Room getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }
}
