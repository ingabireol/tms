package com.mun.theatrems.controller;

import com.mun.theatrems.model.User;
import com.mun.theatrems.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with ID: " + savedUser.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
            .map(user -> ResponseEntity.ok(user))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable UUID id,
            @RequestBody User userDetails) {
        try {
            userService.updateUser(id, userDetails);
            return ResponseEntity.ok("User updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting user");
        }
    }
}
