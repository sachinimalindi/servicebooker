package com.servicebooker.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentAttemptRequest(
        @NotNull UUID bookingId,
        @NotNull BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String provider, // STRIPE/PAYPAL/MANUAL
        String externalRef
) {
}
