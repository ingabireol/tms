package com.mun.theatrems.controller;

import com.mun.theatrems.model.Hall;
import com.mun.theatrems.model.EHallType;
import com.mun.theatrems.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HallController {

    private final HallService hallService;

    @PostMapping
    public ResponseEntity<?> createHall(
            @RequestBody Hall hall,
            @RequestParam UUID theatreId) {
        try {
            Hall createdHall = hallService.createHall(hall, theatreId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHall);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHallById(@PathVariable UUID id) {
        return hallService.getHallById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Hall>> getAllHalls() {
        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<Hall>> getHallsByTheatre(@PathVariable UUID theatreId) {
        return ResponseEntity.ok(hallService.getHallsByTheatre(theatreId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Hall>> getHallsByType(@PathVariable EHallType type) {
        return ResponseEntity.ok(hallService.getHallsByType(type));
    }

    @GetMapping("/theatre/{theatreId}/active")
    public ResponseEntity<List<Hall>> getActiveHallsByTheatre(@PathVariable UUID theatreId) {
        return ResponseEntity.ok(hallService.getActiveHallsByTheatre(theatreId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hall> updateHall(@PathVariable UUID id, @RequestBody Hall hall) {
        try {
            return ResponseEntity.ok(hallService.updateHall(id, hall));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHall(@PathVariable UUID id) {
        hallService.deleteHall(id);
        return ResponseEntity.ok("Hall deactivated successfully");
    }
}
