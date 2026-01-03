package com.servicebooker.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentAttemptResponse(
        UUID id,
        UUID bookingId,
        BigDecimal amount,
        String currency,
        String provider,
        String status,
        String externalRef,
        String failureReason
) {
}
