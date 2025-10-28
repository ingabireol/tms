package com.mun.theatrems.service;

import com.mun.theatrems.model.Hall;
import com.mun.theatrems.model.Theatre;
import com.mun.theatrems.model.EHallType;
import com.mun.theatrems.repository.HallRepository;
import com.mun.theatrems.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HallService {

    private final HallRepository hallRepository;
    private final TheatreRepository theatreRepository;

    public Hall createHall(Hall hall, UUID theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
            .orElseThrow(() -> new RuntimeException("Theatre not found"));

        hall.setTheatre(theatre);
        return hallRepository.save(hall);
    }

    public Optional<Hall> getHallById(UUID id) {
        return hallRepository.findById(id);
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public List<Hall> getHallsByTheatre(UUID theatreId) {
        return hallRepository.findByTheatreId(theatreId);
    }

    public List<Hall> getHallsByType(EHallType hallType) {
        return hallRepository.findByHallType(hallType);
    }

    public List<Hall> getActiveHallsByTheatre(UUID theatreId) {
        return hallRepository.findByTheatreIdAndIsActive(theatreId, true);
    }

    public Hall updateHall(UUID id, Hall hallDetails) {
        Hall hall = hallRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hall not found"));

        hall.setName(hallDetails.getName());
        hall.setTotalSeats(hallDetails.getTotalSeats());
        hall.setHallType(hallDetails.getHallType());

        return hallRepository.save(hall);
    }

    public void deleteHall(UUID id) {
        Hall hall = hallRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hall not found"));
        hall.setIsActive(false);
        hallRepository.save(hall);
    }
}
