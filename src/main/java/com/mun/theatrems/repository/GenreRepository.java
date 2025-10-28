package com.mun.theatrems.repository;

import com.mun.theatrems.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {

    Optional<Genre> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT g FROM Genre g JOIN g.movies m WHERE m.id = ?1")
    List<Genre> findByMovieId(UUID movieId);
}
