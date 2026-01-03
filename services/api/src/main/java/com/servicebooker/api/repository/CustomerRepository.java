package com.servicebooker.api.repository;

import com.servicebooker.api.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Page<Customer> findByBusinessId(UUID businessId, Pageable pageable);
    Optional<Customer> findByBusinessIdAndEmail(UUID businessId, String email);
    Optional<Customer> findByIdAndBusinessId(UUID id, UUID businessId);
}
