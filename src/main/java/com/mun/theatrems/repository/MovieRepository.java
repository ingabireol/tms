package com.mun.theatrems.repository;

import com.mun.theatrems.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    List<Movie> findByIsActive(Boolean isActive);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByDirectorContainingIgnoreCase(String director);

    @Query("SELECT m FROM Movie m WHERE m.genres = ?1")
    List<Movie> findByGenreName(String genreName);
}
