package com.mun.theatrems.service;

import com.mun.theatrems.controller.LocationRequest;
import com.mun.theatrems.exception.DuplicateResourceException;
import com.mun.theatrems.exception.ResourceNotFoundException;
import com.mun.theatrems.exception.ValidationException;
import com.mun.theatrems.model.Location;
import com.mun.theatrems.model.ELocation;
import com.mun.theatrems.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;


    public Location saveLocation(LocationRequest request) {
        // Check if code already exists
        if (locationRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Location", "code", request.getCode());
        }

        Location location = new Location();
        location.setName(request.getName());
        location.setCode(request.getCode());
        location.setType(request.getType());

        // Validation: Province must NOT have parent, others MUST have parent
        if (request.getType() == ELocation.PROVINCE) {
            // Province should not have a parent
            if (request.getParentCode() != null && !request.getParentCode().isEmpty()) {
                throw new ValidationException("Province cannot have a parent location");
            }
            location.setParent(null);
        } else {
            // All other types must have a parent
            if (request.getParentCode() == null || request.getParentCode().isEmpty()) {
                throw new ValidationException(
                        String.format("%s must have a parent location", request.getType()));
            }

            // Find and validate parent
            Location parent = locationRepository.findByCode(request.getParentCode())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Location", "code", request.getParentCode()));

            // Validate hierarchy
            if (!isValidHierarchy(parent.getType(), request.getType())) {
                throw new ValidationException(
                        String.format("Invalid hierarchy: %s cannot have child of type %s",
                                parent.getType(), request.getType()));
            }

            location.setParent(parent);
        }

        return locationRepository.save(location);
    }

    /**
     * Get all provinces
     */
    public List<Location> getAllProvinces() {
        return locationRepository.findByType(ELocation.PROVINCE);
    }

    public Map<String, Object> getLocationHierarchy(String code) {
        Location location = getLocationByCode(code);
        return buildLocationHierarchy(location);
    }

    public List<Map<String, Object>> getCompleteHierarchy() {
        List<Location> provinces = getAllProvinces();
        return provinces.stream()
                .map(this::buildLocationHierarchy)
                .toList();
    }

    public Location getLocationByCode(String code) {
        return locationRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "code", code));
    }

    public Location getLocationById(UUID id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public List<Location> getLocationsByType(ELocation type) {
        return locationRepository.findByType(type);
    }

    public List<Location> getChildLocations(String parentCode) {
        Location parent = locationRepository.findByCode(parentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "code", parentCode));

        return locationRepository.findByParent(parent);
    }

    public String getProvinceByLocationCode(String code) {
        Location location = getLocationByCode(code);

        // Traverse up the hierarchy until we reach the root (Province)
        while (location.getParent() != null) {
            location = location.getParent();
        }

        return location.getName();
    }

    public String getLocationPath(String code) {
        Location location = getLocationByCode(code);

        List<String> pathParts = new ArrayList<>();

        // Build path from current location up to root
        while (location != null) {
            pathParts.add(0, location.getName());
            location = location.getParent();
        }

        return String.join(" > ", pathParts);
    }

    /**
     * Update location
     * @throws ResourceNotFoundException if location not found
     */
    public Location updateLocation(UUID id, Location locationDetails) {
        Location location = getLocationById(id);

        location.setName(locationDetails.getName());
        // Note: We don't update code, type, or parent to maintain data integrity

        return locationRepository.save(location);
    }

    /**
     * Delete location
     * @throws ResourceNotFoundException if location not found
     * @throws ValidationException if location has children
     */
    public void deleteLocation(UUID id) {
        Location location = getLocationById(id);

        // Check if location has children
        List<Location> children = locationRepository.findByParent(location);
        if (!children.isEmpty()) {
            throw new ValidationException(
                    String.format("Cannot delete location with %d child location(s). Delete children first.",
                            children.size()));
        }

        locationRepository.delete(location);
    }

    /**
     * Count locations by type
     */
    public long countLocationsByType(ELocation type) {
        return locationRepository.findByType(type).size();
    }

    /**
     * Search locations by name (case-insensitive)
     */
    public List<Location> searchLocationsByName(String name) {
        return locationRepository.findAll().stream()
                .filter(location -> location.getName().toLowerCase()
                        .contains(name.toLowerCase()))
                .toList();
    }

    /**
     * Validate location hierarchy
     * @throws ResourceNotFoundException if parent not found
     */
    public boolean validateHierarchy(String parentCode, ELocation childType) {
        Location parent = getLocationByCode(parentCode);
        return isValidHierarchy(parent.getType(), childType);
    }

    /**
     * Get location statistics
     */
    public Map<String, Object> getLocationStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalLocations", locationRepository.count());
        stats.put("provinces", countLocationsByType(ELocation.PROVINCE));
        stats.put("districts", countLocationsByType(ELocation.DISTRICT));
        stats.put("sectors", countLocationsByType(ELocation.SECTOR));
        stats.put("cells", countLocationsByType(ELocation.CELL));
        stats.put("villages", countLocationsByType(ELocation.VILLAGE));

        return stats;
    }

    /**
     * Get depth of location in hierarchy (0 = Province, 4 = Village)
     * @throws ResourceNotFoundException if location not found
     */
    public int getLocationDepth(String code) {
        Location location = getLocationByCode(code);

        int depth = 0;
        while (location.getParent() != null) {
            depth++;
            location = location.getParent();
        }

        return depth;
    }

    public List<Location> getAllDescendants(String code) {
        Location location = getLocationByCode(code);
        return getAllDescendantsRecursive(location);
    }

    public boolean existsByCode(String code) {
        return locationRepository.existsByCode(code);
    }


    private boolean isValidHierarchy(ELocation parentType, ELocation childType) {
        return switch (parentType) {
            case PROVINCE -> childType == ELocation.DISTRICT;
            case DISTRICT -> childType == ELocation.SECTOR;
            case SECTOR -> childType == ELocation.CELL;
            case CELL -> childType == ELocation.VILLAGE;
            case VILLAGE -> false;  // Village cannot have children
        };
    }

    /**
     * Build a hierarchical structure for a location and all its descendants
     * Recursively includes all children at all levels
     */
    private Map<String, Object> buildLocationHierarchy(Location location) {
        Map<String, Object> hierarchy = new HashMap<>();
        hierarchy.put("id", location.getId());
        hierarchy.put("name", location.getName());
        hierarchy.put("code", location.getCode());
        hierarchy.put("type", location.getType());

        // Get direct children
        List<Location> children = locationRepository.findByParent(location);

        if (!children.isEmpty()) {
            // Recursively build hierarchy for each child
            List<Map<String, Object>> childHierarchies = children.stream()
                    .map(this::buildLocationHierarchy)
                    .toList();

            hierarchy.put("children", childHierarchies);
            hierarchy.put("childCount", children.size());

            // Add type-specific keys for better clarity
            switch (location.getType()) {
                case PROVINCE -> hierarchy.put("districts", childHierarchies);
                case DISTRICT -> hierarchy.put("sectors", childHierarchies);
                case SECTOR -> hierarchy.put("cells", childHierarchies);
                case CELL -> hierarchy.put("villages", childHierarchies);
            }
        }

        return hierarchy;
    }

    /**
     * Recursively get all descendants
     */
    private List<Location> getAllDescendantsRecursive(Location location) {
        List<Location> descendants = new ArrayList<>();
        List<Location> children = locationRepository.findByParent(location);

        descendants.addAll(children);

        // Add children's children
        for (Location child : children) {
            descendants.addAll(getAllDescendantsRecursive(child));
        }

        return descendants;
    }
}