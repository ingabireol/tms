package com.mun.theatrems.repository;

import com.mun.theatrems.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    List<Screening> findByMovieId(UUID movieId);

    List<Screening> findByTheatreId(UUID theatreId);

    List<Screening> findByHallId(UUID hallId);

    List<Screening> findByScreeningDate(LocalDate date);

    List<Screening> findByIsActive(Boolean isActive);

    @Query("SELECT s FROM Screening s WHERE s.movie.id = ?1 AND s.theatre.id = ?2 AND s.isActive = true")
    List<Screening> findActiveScreeningsByMovieAndTheatre(UUID movieId, UUID theatreId);

    @Query("SELECT s FROM Screening s WHERE s.screeningDate BETWEEN ?1 AND ?2")
    List<Screening> findByDateRange(LocalDate startDate, LocalDate endDate);
}
