package com.servicebooker.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateServiceRequest(
        @NotNull UUID businessId,
        @NotBlank String name,
        @Size(max = 2000) String description,
        @Min(1) int durationMinutes,
        @NotNull BigDecimal priceAmount,
        @NotBlank String priceCurrency,
        @NotBlank String depositType, // NONE/FIXED/PERCENT
        BigDecimal depositValue
) {
}
