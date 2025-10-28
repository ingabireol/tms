package com.mun.theatrems.repository;

import com.mun.theatrems.model.Booking;
import com.mun.theatrems.model.EBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByUserId(UUID userId);

    List<Booking> findByScreeningId(UUID screeningId);

    List<Booking> findByBookingStatus(EBookingStatus status);

    List<Booking> findByUserIdAndBookingStatus(UUID userId, EBookingStatus status);
}
