package com.mun.theatrems.repository;

import com.mun.theatrems.model.Seat;
import com.mun.theatrems.model.ESeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

    List<Seat> findByHallId(UUID hallId);

    List<Seat> findBySeatType(ESeatType seatType);

    List<Seat> findByHallIdAndSeatType(UUID hallId, ESeatType seatType);
}
