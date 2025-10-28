package com.mun.theatrems.repository;

import com.mun.theatrems.model.UserProfile;
import com.mun.theatrems.model.EMembershipLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUserId(UUID userId);

    List<UserProfile> findByMembershipLevel(EMembershipLevel level);

    List<UserProfile> findByLoyaltyPointsGreaterThan(Integer points);
}
