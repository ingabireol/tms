package com.mun.theatrems.controller;

import com.mun.theatrems.model.Genre;
import com.mun.theatrems.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<?> createGenre(@RequestBody Genre genre) {
        try {
            Genre createdGenre = genreService.createGenre(genre);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable UUID id) {
        return genreService.getGenreById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getGenreByName(@PathVariable String name) {
        return genreService.getGenreByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Genre>> getGenresByMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(genreService.getGenresByMovie(movieId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable UUID id, @RequestBody Genre genre) {
        try {
            return ResponseEntity.ok(genreService.updateGenre(id, genre));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable UUID id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok("Genre deleted successfully");
    }
}
