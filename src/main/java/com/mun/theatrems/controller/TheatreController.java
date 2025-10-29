package com.mun.theatrems.controller;

import com.mun.theatrems.model.Theatre;
import com.mun.theatrems.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TheatreController {

    private final TheatreService theatreService;

    @PostMapping
    public ResponseEntity<?> createTheatre(
            @RequestBody Theatre theatre,
            @RequestParam String locationCode) {
        try {
            Theatre createdTheatre = theatreService.createTheatre(theatre, locationCode);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTheatre);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTheatreById(@PathVariable UUID id) {
        return theatreService.getTheatreById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Theatre>> getActiveTheatres() {
        return ResponseEntity.ok(theatreService.getActiveTheatres());
    }

    @GetMapping("/location/{locationCode}")
    public ResponseEntity<List<Theatre>> getTheatresByLocation(@PathVariable String locationCode) {
        return ResponseEntity.ok(theatreService.getTheatresByLocation(locationCode));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable UUID id, @RequestBody Theatre theatre) {
        try {
            return ResponseEntity.ok(theatreService.updateTheatre(id, theatre));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTheatre(@PathVariable UUID id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.ok("Theatre deactivated successfully");
    }
}
