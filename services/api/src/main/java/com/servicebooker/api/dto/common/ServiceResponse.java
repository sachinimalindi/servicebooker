package com.servicebooker.api.dto.common;

import java.math.BigDecimal;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        UUID businessId,
        String name,
        String description,
        int durationMinutes,
        BigDecimal priceAmount,
        String priceCurrency,
        String depositType,
        BigDecimal depositValue,
        boolean active
) {
}
