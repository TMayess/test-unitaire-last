package com.example.exo12.repository;

import com.example.exo12.model.Room;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepository {

    public Room save(Room room) { return null; }

    public Optional<Room> findById(String id) { return Optional.empty(); }

    public List<Room> findAll() { return List.of(); }

    public void deleteAll() {}
}
