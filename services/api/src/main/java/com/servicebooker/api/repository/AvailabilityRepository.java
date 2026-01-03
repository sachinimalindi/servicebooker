package com.servicebooker.api.repository;

import com.servicebooker.api.model.AvailabilityRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AvailabilityRepository extends JpaRepository<AvailabilityRule, UUID> {
    List<AvailabilityRule> findByBusinessIdAndActiveTrue(UUID businessId);
    void deleteAllByBusinessId(UUID businessId);
}
