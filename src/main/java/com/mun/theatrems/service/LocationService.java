package com.mun.theatrems.service;

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

/**
 * Service for Location management
 * Handles hierarchical location operations (self-referencing relationship)
 * Hierarchy: Province → District → Sector → Cell → Village
 *
 * All business logic and validation happens here
 * Throws exceptions on errors (handled by GlobalExceptionHandler)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    /**
     * Save a root location (Province)
     * @throws ValidationException if location is not a province
     * @throws DuplicateResourceException if code already exists
     */
    public Location saveProvince(Location location) {
        if (location.getType() != ELocation.PROVINCE) {
            throw new ValidationException("Location must be of type PROVINCE");
        }

        if (locationRepository.existsByCode(location.getCode())) {
            throw new DuplicateResourceException("Location", "code", location.getCode());
        }

        location.setParent(null);  // Ensure no parent for province
        return locationRepository.save(location);
    }

    /**
     * Save a child location with parent hierarchy validation
     * @throws ResourceNotFoundException if parent not found
     * @throws ValidationException if hierarchy is invalid
     * @throws DuplicateResourceException if code already exists
     */
    public Location saveChild(String parentCode, Location location) {
        if (parentCode == null || parentCode.isEmpty()) {
            return saveProvince(location);
        }

        Location parent = locationRepository.findByCode(parentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "code", parentCode));

        // Validate hierarchy
        if (!isValidHierarchy(parent.getType(), location.getType())) {
            throw new ValidationException(
                    String.format("Invalid hierarchy: %s cannot have child of type %s",
                            parent.getType(), location.getType()));
        }

        if (locationRepository.existsByCode(location.getCode())) {
            throw new DuplicateResourceException("Location", "code", location.getCode());
        }

        location.setParent(parent);
        return locationRepository.save(location);
    }

    /**
     * Get location by code
     * @throws ResourceNotFoundException if not found
     */
    public Location getLocationByCode(String code) {
        return locationRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "code", code));
    }

    /**
     * Get location by ID
     * @throws ResourceNotFoundException if not found
     */
    public Location getLocationById(UUID id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));
    }

    /**
     * Get all locations
     */
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * Get all locations by type
     */
    public List<Location> getLocationsByType(ELocation type) {
        return locationRepository.findByType(type);
    }

    /**
     * Get child locations for a parent location
     * @throws ResourceNotFoundException if parent not found
     */
    public List<Location> getChildLocations(String parentCode) {
        Location parent = locationRepository.findByCode(parentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "code", parentCode));

        return locationRepository.findByParent(parent);
    }

    /**
     * Get all root locations (locations without parents)
     */
    public List<Location> getRootLocations() {
        return locationRepository.findAll().stream()
                .filter(location -> location.getParent() == null)
                .toList();
    }

    /**
     * Get root locations by type
     */
    public List<Location> getRootLocationsByType(ELocation type) {
        return locationRepository.findRootLocationsByType(type);
    }

    /**
     * Get province name from any location by traversing up the hierarchy
     * @throws ResourceNotFoundException if location not found
     */
    public String getProvinceByLocationCode(String code) {
        Location location = getLocationByCode(code);

        // Traverse up the hierarchy until we reach the root (Province)
        while (location.getParent() != null) {
            location = location.getParent();
        }

        return location.getName();
    }

    /**
     * Get full hierarchy path for a location
     * Returns: Province > District > Sector > Cell > Village
     * @throws ResourceNotFoundException if location not found
     */
    public String getLocationPath(String code) {
        Location location = getLocationByCode(code);

        StringBuilder path = new StringBuilder();

        // Build path from current location up to root
        while (location != null) {
            if (path.length() > 0) {
                path.insert(0, " > ");
            }
            path.insert(0, location.getName());
            location = location.getParent();
        }

        return path.toString();
    }

    /**
     * Get location tree structure from a specific location
     * @throws ResourceNotFoundException if location not found
     */
    public Map<String, Object> getLocationTree(String code) {
        Location location = getLocationByCode(code);
        return buildLocationTree(location);
    }

    /**
     * Get full hierarchy tree (all provinces with their children)
     */
    public List<Map<String, Object>> getFullLocationTree() {
        List<Location> provinces = locationRepository.findByType(ELocation.PROVINCE);
        return provinces.stream()
                .map(this::buildLocationTree)
                .toList();
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

    /**
     * Get all descendants of a location (children, grandchildren, etc.)
     * @throws ResourceNotFoundException if location not found
     */
    public List<Location> getAllDescendants(String code) {
        Location location = getLocationByCode(code);
        return getAllDescendantsRecursive(location);
    }

    /**
     * Check if a location exists by code
     */
    public boolean existsByCode(String code) {
        return locationRepository.existsByCode(code);
    }

    // ==================== Private Helper Methods ====================

    /**
     * Validate location hierarchy based on types
     * Province → District → Sector → Cell → Village
     */
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
     * Build a hierarchical tree structure for a location and its children
     */
    private Map<String, Object> buildLocationTree(Location location) {
        Map<String, Object> tree = new HashMap<>();
        tree.put("id", location.getId());
        tree.put("name", location.getName());
        tree.put("code", location.getCode());
        tree.put("type", location.getType());

        List<Location> children = locationRepository.findByParent(location);
        if (!children.isEmpty()) {
            List<Map<String, Object>> childTrees = children.stream()
                    .map(this::buildLocationTree)
                    .toList();
            tree.put("children", childTrees);
            tree.put("childCount", children.size());
        }

        return tree;
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