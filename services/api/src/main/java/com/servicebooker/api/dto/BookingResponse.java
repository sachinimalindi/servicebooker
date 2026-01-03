package com.servicebooker.api.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID businessId,
        UUID serviceId,
        UUID customerId,
        ZonedDateTime startAt,
        ZonedDateTime endAt,
        String status,
        String customerNotes
) {
}
