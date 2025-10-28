package com.mun.theatrems.service;

import com.mun.theatrems.model.Booking;
import com.mun.theatrems.model.Screening;
import com.mun.theatrems.model.User;
import com.mun.theatrems.model.EBookingStatus;
import com.mun.theatrems.repository.BookingRepository;
import com.mun.theatrems.repository.ScreeningRepository;
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
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;

    public Booking createBooking(UUID userId, UUID screeningId, Booking booking) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Screening screening = screeningRepository.findById(screeningId)
            .orElseThrow(() -> new RuntimeException("Screening not found"));

        if (screening.getAvailableSeats() < booking.getNumberOfSeats()) {
            throw new RuntimeException("Not enough seats available");
        }

        booking.setUser(user);
        booking.setScreening(screening);

        screening.setAvailableSeats(screening.getAvailableSeats() - booking.getNumberOfSeats());
        screeningRepository.save(screening);

        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBookingById(UUID id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> getBookingByNumber(String bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber);
    }

    public List<Booking> getBookingsByUser(UUID userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByScreening(UUID screeningId) {
        return bookingRepository.findByScreeningId(screeningId);
    }

    public List<Booking> getBookingsByStatus(EBookingStatus status) {
        return bookingRepository.findByBookingStatus(status);
    }

    public Booking confirmBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingStatus(EBookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        Screening screening = booking.getScreening();
        screening.setAvailableSeats(screening.getAvailableSeats() + booking.getNumberOfSeats());
        screeningRepository.save(screening);

        booking.setBookingStatus(EBookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
}
