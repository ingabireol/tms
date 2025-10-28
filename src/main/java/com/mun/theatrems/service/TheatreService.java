package com.mun.theatrems.service;

import com.mun.theatrems.model.Theatre;
import com.mun.theatrems.model.Location;
import com.mun.theatrems.repository.TheatreRepository;
import com.mun.theatrems.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;

    public Theatre createTheatre(Theatre theatre, String locationCode) {
        Location location = locationRepository.findByCode(locationCode)
            .orElseThrow(() -> new RuntimeException("Location not found"));

        theatre.setLocation(location);
        return theatreRepository.save(theatre);
    }

    public Optional<Theatre> getTheatreById(UUID id) {
        return theatreRepository.findById(id);
    }

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public List<Theatre> getActiveTheatres() {
        return theatreRepository.findByIsActive(true);
    }

    public List<Theatre> getTheatresByLocation(String locationCode) {
        return theatreRepository.findByLocationCode(locationCode);
    }

    public Theatre updateTheatre(UUID id, Theatre theatreDetails) {
        Theatre theatre = theatreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Theatre not found"));

        theatre.setName(theatreDetails.getName());
        theatre.setAddress(theatreDetails.getAddress());
        theatre.setPhoneNumber(theatreDetails.getPhoneNumber());
        theatre.setEmail(theatreDetails.getEmail());
        theatre.setTotalHalls(theatreDetails.getTotalHalls());
        theatre.setFacilities(theatreDetails.getFacilities());

        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(UUID id) {
        Theatre theatre = theatreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Theatre not found"));
        theatre.setIsActive(false);
        theatreRepository.save(theatre);
    }
}
