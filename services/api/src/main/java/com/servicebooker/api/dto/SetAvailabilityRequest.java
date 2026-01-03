package com.servicebooker.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SetAvailabilityRequest(
        @NotNull UUID businessId,
        @NotEmpty List<Rule> rules
) {

    public record Rule(
            @NotNull DayOfWeek dayOfWeek,
            @NotNull LocalTime startTime,
            @NotNull LocalTime endTime,
            @Min(0) int bufferMinutesBefore,
            @Min(0) int bufferMinutesAfter,
            boolean active
    ) {}
}
