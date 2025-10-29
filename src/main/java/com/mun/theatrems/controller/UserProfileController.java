package com.mun.theatrems.controller;

import com.mun.theatrems.model.UserProfile;
import com.mun.theatrems.model.EMembershipLevel;
import com.mun.theatrems.service.UserService;
import com.mun.theatrems.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for UserProfile management
 * Handles profile operations for users (One-to-One relationship)
 */
@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserService userService;
    private final UserProfileRepository userProfileRepository;

    /**
     * Create profile for existing user (One-to-One)
     * POST /api/user-profiles/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createUserProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfile profile) {
        try {
            UserProfile createdProfile = userService.addUserProfile(userId, profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Get profile by user ID (One-to-One)
     * GET /api/user-profiles/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable UUID userId) {
        return userProfileRepository.findByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Get all user profiles
     * GET /api/user-profiles
     */
    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllProfiles() {
        return ResponseEntity.ok(userProfileRepository.findAll());
    }

    /**
     * Get profiles by membership level
     * GET /api/user-profiles/membership/{level}
     */
    @GetMapping("/membership/{level}")
    public ResponseEntity<List<UserProfile>> getProfilesByMembershipLevel(
            @PathVariable EMembershipLevel level) {
        return ResponseEntity.ok(userProfileRepository.findByMembershipLevel(level));
    }

    /**
     * Get profiles with loyalty points greater than specified amount
     * GET /api/user-profiles/loyalty-points/{points}
     */
    @GetMapping("/loyalty-points/{points}")
    public ResponseEntity<List<UserProfile>> getProfilesByLoyaltyPoints(
            @PathVariable Integer points) {
        return ResponseEntity.ok(userProfileRepository.findByLoyaltyPointsGreaterThan(points));
    }

    /**
     * Update user profile
     * PUT /api/user-profiles/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfile profileDetails) {
        try {
            UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

            // Update profile fields
            profile.setDateOfBirth(profileDetails.getDateOfBirth());
            profile.setAddress(profileDetails.getAddress());
            profile.setCity(profileDetails.getCity());
            profile.setCountry(profileDetails.getCountry());
            profile.setPostalCode(profileDetails.getPostalCode());
            profile.setPreferredLanguage(profileDetails.getPreferredLanguage());
            profile.setLoyaltyPoints(profileDetails.getLoyaltyPoints());
            profile.setMembershipLevel(profileDetails.getMembershipLevel());

            UserProfile updatedProfile = userProfileRepository.save(profile);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Delete user profile
     * DELETE /api/user-profiles/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable UUID userId) {
        try {
            userProfileRepository.findByUserId(userId)
                .ifPresentOrElse(
                    profile -> userProfileRepository.delete(profile),
                    () -> { throw new RuntimeException("Profile not found"); }
                );
            return ResponseEntity.ok("Profile deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Update membership level
     * PUT /api/user-profiles/{userId}/membership
     */
    @PutMapping("/{userId}/membership")
    public ResponseEntity<?> updateMembershipLevel(
            @PathVariable UUID userId,
            @RequestParam EMembershipLevel level) {
        try {
            UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

            profile.setMembershipLevel(level);
            UserProfile updatedProfile = userProfileRepository.save(profile);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Update loyalty points
     * PUT /api/user-profiles/{userId}/loyalty-points
     */
    @PutMapping("/{userId}/loyalty-points")
    public ResponseEntity<?> updateLoyaltyPoints(
            @PathVariable UUID userId,
            @RequestParam Integer points) {
        try {
            UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

            profile.setLoyaltyPoints(points);
            UserProfile updatedProfile = userProfileRepository.save(profile);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Add loyalty points to existing total
     * POST /api/user-profiles/{userId}/add-points
     */
    @PostMapping("/{userId}/add-points")
    public ResponseEntity<?> addLoyaltyPoints(
            @PathVariable UUID userId,
            @RequestParam Integer points) {
        try {
            UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

            profile.setLoyaltyPoints(profile.getLoyaltyPoints() + points);
            UserProfile updatedProfile = userProfileRepository.save(profile);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}