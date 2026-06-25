package com.example.exo12.service;

import com.example.exo12.dto.CreateRoomRequest;
import com.example.exo12.model.Room;
import com.example.exo12.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room createRoom(CreateRoomRequest request) { return null; }

    public List<Room> getAllRooms() { return null; }

    public Room getById(String id) { return null; }
}
