package com.example.exo12.controller;

import com.example.exo12.dto.CreateRoomRequest;
import com.example.exo12.model.Room;
import com.example.exo12.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Room> create(@Valid @RequestBody CreateRoomRequest request) { return null; }

    @GetMapping
    public ResponseEntity<List<Room>> getAll() { return null; }
}
