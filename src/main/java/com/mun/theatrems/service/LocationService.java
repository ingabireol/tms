package com.mun.theatrems.service;

import com.mun.theatrems.model.Location;
import com.mun.theatrems.model.ELocation;
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
public class LocationService {

    private final LocationRepository locationRepository;

    public String saveProvince(Location location) {
        if (location.getType() != ELocation.PROVINCE) {
            return "Location must be of type Province";
        }
        if (locationRepository.existsByCode(location.getCode())) {
            return "Location with this code already exists";
        }
        location.setParent(null);
        locationRepository.save(location);
        return "Province saved successfully";
    }

    public String saveChild(String parentCode, Location location) {
        if (parentCode == null || parentCode.isEmpty()) {
            return saveProvince(location);
        }

        Optional<Location> parentOpt = locationRepository.findByCode(parentCode);
        if (parentOpt.isEmpty()) {
            return "Parent location with code " + parentCode + " does not exist";
        }

        Location parent = parentOpt.get();

        if (!isValidHierarchy(parent.getType(), location.getType())) {
            return "Invalid location hierarchy";
        }

        if (locationRepository.existsByCode(location.getCode())) {
            return "Location with this code already exists";
        }

        location.setParent(parent);
        locationRepository.save(location);
        return "Child location saved successfully";
    }

    public Optional<Location> getLocationByCode(String code) {
        return locationRepository.findByCode(code);
    }

    public List<Location> getLocationsByType(ELocation type) {
        return locationRepository.findByType(type);
    }

    public List<Location> getChildLocations(String parentCode) {
        Optional<Location> parent = locationRepository.findByCode(parentCode);
        return parent.map(locationRepository::findByParent)
                    .orElse(List.of());
    }

    public String getProvinceByLocationCode(String code) {
        Optional<Location> locationOpt = locationRepository.findByCode(code);
        if (locationOpt.isEmpty()) {
            return "Location not found";
        }

        Location location = locationOpt.get();
        while (location.getParent() != null) {
            location = location.getParent();
        }

        return location.getName();
    }

    private boolean isValidHierarchy(ELocation parentType, ELocation childType) {
        return switch (parentType) {
            case PROVINCE -> childType == ELocation.DISTRICT;
            case DISTRICT -> childType == ELocation.SECTOR;
            case SECTOR -> childType == ELocation.CELL;
            case CELL -> childType == ELocation.VILLAGE;
            case VILLAGE -> false;
        };
    }

    public String getLocationPath(String code) {
        Optional<Location> locationOpt = locationRepository.findByCode(code);
        if (locationOpt.isEmpty()) {
            return "Location not found";
        }

        StringBuilder path = new StringBuilder();
        Location location = locationOpt.get();

        while (location != null) {
            if (path.length() > 0) {
                path.insert(0, " > ");
            }
            path.insert(0, location.getName());
            location = location.getParent();
        }

        return path.toString();
    }
}
