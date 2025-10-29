package com.mun.theatrems.controller;

import com.mun.theatrems.model.Location;
import com.mun.theatrems.model.ELocation;
import com.mun.theatrems.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Location management
 * Handles hierarchical location system (Province → District → Sector → Cell → Village)
 * Demonstrates self-referencing relationship (parent-child hierarchy)
 *
 * Controller responsibilities:
 * - Receive HTTP requests
 * - Call appropriate service methods
 * - Return HTTP responses
 *
 * All business logic is in LocationService
 * All exceptions are handled by GlobalExceptionHandler
 */
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    /**
     * Create a root location (Province)
     * POST /api/locations/province
     */
    @PostMapping("/province")
    public ResponseEntity<Location> saveProvince(@RequestBody Location location) {
        Location savedLocation = locationService.saveProvince(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    /**
     * Create a child location with parent hierarchy
     * POST /api/locations/child?parentCode={code}
     */
    @PostMapping("/child")
    public ResponseEntity<Location> saveChild(
            @RequestParam String parentCode,
            @RequestBody Location location) {
        Location savedLocation = locationService.saveChild(parentCode, location);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    /**
     * Get location by code
     * GET /api/locations/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Location> getLocationByCode(@PathVariable String code) {
        Location location = locationService.getLocationByCode(code);
        return ResponseEntity.ok(location);
    }

    /**
     * Get location by ID
     * GET /api/locations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable UUID id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    /**
     * Get all locations
     * GET /api/locations
     */
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Get all locations by type
     * GET /api/locations/type/{type}
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Location>> getLocationsByType(@PathVariable ELocation type) {
        List<Location> locations = locationService.getLocationsByType(type);
        return ResponseEntity.ok(locations);
    }

    /**
     * Get child locations for a parent location
     * GET /api/locations/children/{parentCode}
     */
    @GetMapping("/children/{parentCode}")
    public ResponseEntity<List<Location>> getChildLocations(@PathVariable String parentCode) {
        List<Location> children = locationService.getChildLocations(parentCode);
        return ResponseEntity.ok(children);
    }

    /**
     * Get all root locations (locations without parents)
     * GET /api/locations/roots
     */
    @GetMapping("/roots")
    public ResponseEntity<List<Location>> getRootLocations() {
        List<Location> roots = locationService.getRootLocations();
        return ResponseEntity.ok(roots);
    }

    /**
     * Get root locations by type
     * GET /api/locations/roots/type/{type}
     */
    @GetMapping("/roots/type/{type}")
    public ResponseEntity<List<Location>> getRootLocationsByType(@PathVariable ELocation type) {
        List<Location> roots = locationService.getRootLocationsByType(type);
        return ResponseEntity.ok(roots);
    }

    /**
     * Get province name from any location code (traverse up hierarchy)
     * GET /api/locations/province/{code}
     */
    @GetMapping("/province/{code}")
    public ResponseEntity<String> getProvinceByCode(@PathVariable String code) {
        String province = locationService.getProvinceByLocationCode(code);
        return ResponseEntity.ok(province);
    }

    /**
     * Get full hierarchy path for a location
     * GET /api/locations/path/{code}
     */
    @GetMapping("/path/{code}")
    public ResponseEntity<String> getLocationPath(@PathVariable String code) {
        String path = locationService.getLocationPath(code);
        return ResponseEntity.ok(path);
    }

    /**
     * Get location hierarchy tree starting from a specific location
     * GET /api/locations/tree/{code}
     */
    @GetMapping("/tree/{code}")
    public ResponseEntity<Map<String, Object>> getLocationTree(@PathVariable String code) {
        Map<String, Object> tree = locationService.getLocationTree(code);
        return ResponseEntity.ok(tree);
    }

    /**
     * Get full hierarchy tree (all provinces with their children)
     * GET /api/locations/tree
     */
    @GetMapping("/tree")
    public ResponseEntity<List<Map<String, Object>>> getFullLocationTree() {
        List<Map<String, Object>> tree = locationService.getFullLocationTree();
        return ResponseEntity.ok(tree);
    }

    /**
     * Update location
     * PUT /api/locations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(
            @PathVariable UUID id,
            @RequestBody Location locationDetails) {
        Location updatedLocation = locationService.updateLocation(id, locationDetails);
        return ResponseEntity.ok(updatedLocation);
    }

    /**
     * Delete location
     * DELETE /api/locations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Count locations by type
     * GET /api/locations/count/type/{type}
     */
    @GetMapping("/count/type/{type}")
    public ResponseEntity<Long> countLocationsByType(@PathVariable ELocation type) {
        long count = locationService.countLocationsByType(type);
        return ResponseEntity.ok(count);
    }

    /**
     * Search locations by name (case-insensitive)
     * GET /api/locations/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Location>> searchLocationsByName(@RequestParam String name) {
        List<Location> locations = locationService.searchLocationsByName(name);
        return ResponseEntity.ok(locations);
    }

    /**
     * Validate location hierarchy
     * GET /api/locations/validate-hierarchy?parentCode={parentCode}&childType={childType}
     */
    @GetMapping("/validate-hierarchy")
    public ResponseEntity<Boolean> validateHierarchy(
            @RequestParam String parentCode,
            @RequestParam ELocation childType) {
        boolean isValid = locationService.validateHierarchy(parentCode, childType);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Get location statistics
     * GET /api/locations/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getLocationStatistics() {
        Map<String, Object> stats = locationService.getLocationStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get location depth in hierarchy
     * GET /api/locations/depth/{code}
     */
    @GetMapping("/depth/{code}")
    public ResponseEntity<Integer> getLocationDepth(@PathVariable String code) {
        int depth = locationService.getLocationDepth(code);
        return ResponseEntity.ok(depth);
    }

    /**
     * Get all descendants of a location
     * GET /api/locations/descendants/{code}
     */
    @GetMapping("/descendants/{code}")
    public ResponseEntity<List<Location>> getAllDescendants(@PathVariable String code) {
        List<Location> descendants = locationService.getAllDescendants(code);
        return ResponseEntity.ok(descendants);
    }

    /**
     * Check if location exists by code
     * GET /api/locations/exists/{code}
     */
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        boolean exists = locationService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
}