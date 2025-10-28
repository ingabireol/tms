package com.mun.theatrems.service;

import com.mun.theatrems.model.Genre;
import com.mun.theatrems.model.Movie;
import com.mun.theatrems.repository.GenreRepository;
import com.mun.theatrems.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public Genre createGenre(Genre genre) {
        if (genreRepository.existsByNameIgnoreCase(genre.getName())) {
            throw new RuntimeException("Genre already exists");
        }
        return genreRepository.save(genre);
    }

    public Movie addGenreToMovie(UUID movieId, UUID genreId) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        Genre genre = genreRepository.findById(genreId)
            .orElseThrow(() -> new RuntimeException("Genre not found"));

        movie.addGenre(genre);
        return movieRepository.save(movie);
    }

    public Movie removeGenreFromMovie(UUID movieId, UUID genreId) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        Genre genre = genreRepository.findById(genreId)
            .orElseThrow(() -> new RuntimeException("Genre not found"));

        movie.removeGenre(genre);
        return movieRepository.save(movie);
    }

    public List<Genre> getGenresByMovie(UUID movieId) {
        return genreRepository.findByMovieId(movieId);
    }

    public Optional<Genre> getGenreById(UUID id) {
        return genreRepository.findById(id);
    }

    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre updateGenre(UUID id, Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genre not found"));

        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());

        return genreRepository.save(genre);
    }

    public void deleteGenre(UUID id) {
        genreRepository.deleteById(id);
    }
}
