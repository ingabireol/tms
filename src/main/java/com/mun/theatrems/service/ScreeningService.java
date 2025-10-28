package com.mun.theatrems.service;

import com.mun.theatrems.model.Screening;
import com.mun.theatrems.model.Movie;
import com.mun.theatrems.model.Theatre;
import com.mun.theatrems.model.Hall;
import com.mun.theatrems.repository.ScreeningRepository;
import com.mun.theatrems.repository.MovieRepository;
import com.mun.theatrems.repository.TheatreRepository;
import com.mun.theatrems.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final HallRepository hallRepository;

    public Screening createScreening(UUID movieId, UUID theatreId, UUID hallId, Screening screening) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        Theatre theatre = theatreRepository.findById(theatreId)
            .orElseThrow(() -> new RuntimeException("Theatre not found"));
        Hall hall = hallRepository.findById(hallId)
            .orElseThrow(() -> new RuntimeException("Hall not found"));

        screening.setMovie(movie);
        screening.setTheatre(theatre);
        screening.setHall(hall);
        screening.setAvailableSeats(hall.getTotalSeats());

        return screeningRepository.save(screening);
    }

    public Optional<Screening> getScreeningById(UUID id) {
        return screeningRepository.findById(id);
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public List<Screening> getScreeningsByMovie(UUID movieId) {
        return screeningRepository.findByMovieId(movieId);
    }

    public List<Screening> getScreeningsByTheatre(UUID theatreId) {
        return screeningRepository.findByTheatreId(theatreId);
    }

    public List<Screening> getScreeningsByDate(LocalDate date) {
        return screeningRepository.findByScreeningDate(date);
    }

    public List<Screening> getActiveScreeningsByMovieAndTheatre(UUID movieId, UUID theatreId) {
        return screeningRepository.findActiveScreeningsByMovieAndTheatre(movieId, theatreId);
    }

    public List<Screening> getScreeningsByDateRange(LocalDate startDate, LocalDate endDate) {
        return screeningRepository.findByDateRange(startDate, endDate);
    }

    public Screening updateScreening(UUID id, Screening screeningDetails) {
        Screening screening = screeningRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Screening not found"));

        screening.setScreeningDate(screeningDetails.getScreeningDate());
        screening.setStartTime(screeningDetails.getStartTime());
        screening.setEndTime(screeningDetails.getEndTime());
        screening.setBasePrice(screeningDetails.getBasePrice());
        screening.setScreeningType(screeningDetails.getScreeningType());

        return screeningRepository.save(screening);
    }

    public void cancelScreening(UUID id) {
        Screening screening = screeningRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Screening not found"));
        screening.setIsActive(false);
        screeningRepository.save(screening);
    }
}
