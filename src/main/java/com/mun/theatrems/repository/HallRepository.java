package com.mun.theatrems.repository;

import com.mun.theatrems.model.Hall;
import com.mun.theatrems.model.EHallType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HallRepository extends JpaRepository<Hall, UUID> {

    List<Hall> findByTheatreId(UUID theatreId);

    List<Hall> findByHallType(EHallType hallType);

    List<Hall> findByTheatreIdAndIsActive(UUID theatreId, Boolean isActive);
}
