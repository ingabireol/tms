package com.mun.theatrems.controller;

import com.mun.theatrems.model.Screening;
import com.mun.theatrems.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/screenings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScreeningController {

    private final ScreeningService screeningService;

    @PostMapping
    public ResponseEntity<?> createScreening(
            @RequestParam UUID movieId,
            @RequestParam UUID theatreId,
            @RequestParam UUID hallId,
            @RequestBody Screening screening) {
        try {
            Screening createdScreening = screeningService.createScreening(movieId, theatreId, hallId, screening);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdScreening);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable UUID id) {
        return screeningService.getScreeningById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Screening>> getAllScreenings() {
        return ResponseEntity.ok(screeningService.getAllScreenings());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Screening>> getScreeningsByMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(screeningService.getScreeningsByMovie(movieId));
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<Screening>> getScreeningsByTheatre(@PathVariable UUID theatreId) {
        return ResponseEntity.ok(screeningService.getScreeningsByTheatre(theatreId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Screening>> getScreeningsByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(screeningService.getScreeningsByDate(date));
    }

    @GetMapping("/movie/{movieId}/theatre/{theatreId}")
    public ResponseEntity<List<Screening>> getActiveScreeningsByMovieAndTheatre(
            @PathVariable UUID movieId,
            @PathVariable UUID theatreId) {
        return ResponseEntity.ok(screeningService.getActiveScreeningsByMovieAndTheatre(movieId, theatreId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Screening>> getScreeningsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(screeningService.getScreeningsByDateRange(startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Screening> updateScreening(@PathVariable UUID id, @RequestBody Screening screening) {
        try {
            return ResponseEntity.ok(screeningService.updateScreening(id, screening));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelScreening(@PathVariable UUID id) {
        screeningService.cancelScreening(id);
        return ResponseEntity.ok("Screening cancelled successfully");
    }
}
