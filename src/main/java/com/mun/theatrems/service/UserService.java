package com.mun.theatrems.service;

import com.mun.theatrems.controller.UserRegistrationRequest;
import com.mun.theatrems.model.Location;
import com.mun.theatrems.model.User;
import com.mun.theatrems.repository.LocationRepository;
import com.mun.theatrems.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    public User registerUser(UserRegistrationRequest request) {
        // Validate username and email uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Build user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        // Add location if provided
        if (request.getLocationCode() != null && !request.getLocationCode().isEmpty()) {
            Location location = locationRepository.findByCode(request.getLocationCode())
                    .orElseThrow(() -> new RuntimeException("Location not found with code: " + request.getLocationCode()));
            user.setLocation(location);
        }

        return userRepository.save(user);
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }
    public List<User> getUsersByLocation(String locationCode) {
        Location location = locationRepository.findByCode(locationCode)
                .orElseThrow(() -> new RuntimeException("Location not found with code: " + locationCode));
        return userRepository.findByLocation(location);
    }

    /**
     * Get users by location ID
     * @param locationId Location ID
     * @return List of users in that location
     */
    public List<User> getUsersByLocationId(UUID locationId) {
        return userRepository.findByLocationId(locationId);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(UUID id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhoneNumber(userDetails.getPhoneNumber());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
