package com.example.exo12.repository;

import com.example.exo12.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRepository {

    private final Map<String, Room> store = new ConcurrentHashMap<>();

    public Room save(Room room) {
        store.put(room.getId(), room);
        return room;
    }

    public Optional<Room> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Room> findAll() {
        return new ArrayList<>(store.values());
    }

    public void deleteAll() {
        store.clear();
    }
}
