package com.servicebooker.api.repository;

import com.servicebooker.api.model.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceOffering, UUID> {
    List<ServiceOffering> findByBusinessIdAndActiveTrue(UUID businessId);
    Optional<ServiceOffering> findByIdAndBusinessId(UUID id, UUID businessId);
}
