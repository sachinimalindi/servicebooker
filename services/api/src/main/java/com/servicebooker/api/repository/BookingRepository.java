package com.servicebooker.api.repository;

import com.servicebooker.api.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findByBusinessId(UUID businessId, Pageable pageable);

    @Query("""
    select (count(b) > 0) from Booking b
    where b.businessId = :businessId
      and b.status <> :excludeStatus
      and b.start < :endAt
      and b.end   > :startAt
  """)
    boolean existsOverlapping(
            @Param("businessId") UUID businessId,
            @Param("startAt") ZonedDateTime startAt,
            @Param("endAt") ZonedDateTime endAt,
            @Param("excludeStatus") Booking.BookingStatus excludeStatus
    );

    Optional<Booking> findByIdAndBusinessId(UUID id, UUID businessId);
}
