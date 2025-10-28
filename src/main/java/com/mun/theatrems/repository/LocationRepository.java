package com.mun.theatrems.repository;

import com.mun.theatrems.model.Location;
import com.mun.theatrems.model.ELocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    Optional<Location> findByCode(String code);

    boolean existsByCode(String code);

    List<Location> findByType(ELocation type);

    List<Location> findByParent(Location parent);

    @Query("SELECT l FROM Location l WHERE l.parent.id = ?1")
    List<Location> findByParentId(UUID parentId);

    @Query("SELECT l FROM Location l WHERE l.type = ?1 AND l.parent IS NULL")
    List<Location> findRootLocationsByType(ELocation type);
}
