package com.servicebooker.api.repository;

import com.servicebooker.api.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository  extends JpaRepository<Business, UUID> {
    Optional<Business> findByIdAndActiveTrue(UUID id);
}
