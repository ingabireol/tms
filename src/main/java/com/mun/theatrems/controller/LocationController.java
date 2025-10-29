package com.mun.theatrems.controller;

import com.mun.theatrems.model.Location;
import com.mun.theatrems.model.ELocation;
import com.mun.theatrems.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/province")
    public ResponseEntity<String> saveProvince(@RequestBody Location location) {
        String response = locationService.saveProvince(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/child")
    public ResponseEntity<String> saveChild(
            @RequestParam String parentCode,
            @RequestBody Location location) {
        String response = locationService.saveChild(parentCode, location);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getLocationByCode(@PathVariable String code) {
        return locationService.getLocationByCode(code)
            .map(location -> ResponseEntity.ok(location))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Location not found"));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Location>> getLocationsByType(
            @PathVariable ELocation type) {
        List<Location> locations = locationService.getLocationsByType(type);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/children/{parentCode}")
    public ResponseEntity<List<Location>> getChildLocations(
            @PathVariable String parentCode) {
        List<Location> children = locationService.getChildLocations(parentCode);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/province/{code}")
    public ResponseEntity<String> getProvinceByCode(@PathVariable String code) {
        String province = locationService.getProvinceByLocationCode(code);
        return ResponseEntity.ok(province);
    }

    @GetMapping("/path/{code}")
    public ResponseEntity<String> getLocationPath(@PathVariable String code) {
        String path = locationService.getLocationPath(code);
        return ResponseEntity.ok(path);
    }
}
