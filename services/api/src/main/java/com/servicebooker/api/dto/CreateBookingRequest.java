package com.servicebooker.api.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;
import java.util.UUID;

public record CreateBookingRequest(
        @NotNull UUID businessId,
        @NotNull UUID serviceId,
        @NotNull UUID customerId,
        @NotNull ZonedDateTime startAt,
        @NotNull ZonedDateTime endAt,
        @Size(max = 2000) String customerNotes
) {
}
