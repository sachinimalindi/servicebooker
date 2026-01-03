package com.servicebooker.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record SlotQueryParams(
        @NotNull UUID businessId,
        @NotNull UUID serviceId,
        @NotNull LocalDate date
) {
}
