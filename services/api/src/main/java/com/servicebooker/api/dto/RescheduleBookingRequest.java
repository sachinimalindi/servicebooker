package com.servicebooker.api.dto;


import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public record RescheduleBookingRequest(
        @NotNull ZonedDateTime startAt,
        @NotNull ZonedDateTime endAt
) {
}
