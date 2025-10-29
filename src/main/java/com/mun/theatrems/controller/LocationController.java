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

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> saveLocation(@RequestBody LocationRequest request) {
        Location savedLocation = locationService.saveLocation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Location> getLocationByCode(@PathVariable String code) {
        Location location = locationService.getLocationByCode(code);
        return ResponseEntity.ok(location);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable UUID id) {
        Location location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Location>> getLocationsByType(@PathVariable ELocation type) {
        List<Location> locations = locationService.getLocationsByType(type);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/children/{parentCode}")
    public ResponseEntity<List<Location>> getChildLocations(@PathVariable String parentCode) {
        List<Location> children = locationService.getChildLocations(parentCode);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<Location>> getAllProvinces() {
        List<Location> provinces = locationService.getAllProvinces();
        return ResponseEntity.ok(provinces);
    }

    @GetMapping("/province/{provinceCode}/hierarchy")
    public ResponseEntity<Map<String, Object>> getProvinceWithDistricts(@PathVariable String provinceCode) {
        Map<String, Object> hierarchy = locationService.getLocationHierarchy(provinceCode);
        return ResponseEntity.ok(hierarchy);
    }

    @GetMapping("/district/{districtCode}/hierarchy")
    public ResponseEntity<Map<String, Object>> getDistrictWithSectors(@PathVariable String districtCode) {
        Map<String, Object> hierarchy = locationService.getLocationHierarchy(districtCode);
        return ResponseEntity.ok(hierarchy);
    }


    @GetMapping("/sector/{sectorCode}/hierarchy")
    public ResponseEntity<Map<String, Object>> getSectorWithCells(@PathVariable String sectorCode) {
        Map<String, Object> hierarchy = locationService.getLocationHierarchy(sectorCode);
        return ResponseEntity.ok(hierarchy);
    }

    @GetMapping("/cell/{cellCode}/hierarchy")
    public ResponseEntity<Map<String, Object>> getCellWithVillages(@PathVariable String cellCode) {
        Map<String, Object> hierarchy = locationService.getLocationHierarchy(cellCode);
        return ResponseEntity.ok(hierarchy);
    }

    @GetMapping("/province-name/{code}")
    public ResponseEntity<String> getProvinceByCode(@PathVariable String code) {
        String province = locationService.getProvinceByLocationCode(code);
        return ResponseEntity.ok(province);
    }

    @GetMapping("/path/{code}")
    public ResponseEntity<String> getLocationPath(@PathVariable String code) {
        String path = locationService.getLocationPath(code);
        return ResponseEntity.ok(path);
    }

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
    @GetMapping("/search")
    public ResponseEntity<List<Location>> searchLocationsByName(@RequestParam String name) {
        List<Location> locations = locationService.searchLocationsByName(name);
        return ResponseEntity.ok(locations);
    }



    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        boolean exists = locationService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
}