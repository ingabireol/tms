package com.mun.theatrems.repository;

import com.mun.theatrems.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, UUID> {

    List<Theatre> findByIsActive(Boolean isActive);

    List<Theatre> findByNameContainingIgnoreCase(String name);

    @Query("SELECT t FROM Theatre t WHERE t.location.code = ?1")
    List<Theatre> findByLocationCode(String locationCode);

    List<Theatre> findByLocationId(UUID locationId);
}
