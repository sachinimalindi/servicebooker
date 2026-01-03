package com.servicebooker.api.dto;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        UUID businessId,
        String fullName,
        String email,
        String phone
) {
}
