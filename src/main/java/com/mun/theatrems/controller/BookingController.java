package com.mun.theatrems.controller;

import com.mun.theatrems.model.Booking;
import com.mun.theatrems.model.EBookingStatus;
import com.mun.theatrems.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestParam UUID userId,
            @RequestParam UUID screeningId,
            @RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(userId, screeningId, booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable UUID id) {
        return bookingService.getBookingById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/number/{bookingNumber}")
    public ResponseEntity<?> getBookingByNumber(@PathVariable String bookingNumber) {
        return bookingService.getBookingByNumber(bookingNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @GetMapping("/screening/{screeningId}")
    public ResponseEntity<List<Booking>> getBookingsByScreening(@PathVariable UUID screeningId) {
        return ResponseEntity.ok(bookingService.getBookingsByScreening(screeningId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable EBookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(bookingService.confirmBooking(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(bookingService.cancelBooking(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
