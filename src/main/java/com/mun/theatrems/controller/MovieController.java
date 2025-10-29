package com.mun.theatrems.controller;

import com.mun.theatrems.model.Movie;
import com.mun.theatrems.service.MovieService;
import com.mun.theatrems.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @PostMapping("/{movieId}/genres/{genreId}")
    public ResponseEntity<?> addGenreToMovie(
            @PathVariable UUID movieId,
            @PathVariable UUID genreId) {
        try {
            Movie movie = genreService.addGenreToMovie(movieId, genreId);
            return ResponseEntity.ok(movie);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{movieId}/genres/{genreId}")
    public ResponseEntity<String> removeGenreFromMovie(
            @PathVariable UUID movieId,
            @PathVariable UUID genreId) {
        try {
            genreService.removeGenreFromMovie(movieId, genreId);
            return ResponseEntity.ok("Genre removed from movie");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable UUID id) {
        return movieService.getMovieById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Movie>> getActiveMovies() {
        return ResponseEntity.ok(movieService.getActiveMovies());
    }

    @GetMapping("/genre/{genreName}")
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genreName) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genreName));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(title));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable UUID id, @RequestBody Movie movie) {
        try {
            return ResponseEntity.ok(movieService.updateMovie(id, movie));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deactivated successfully");
    }
}
