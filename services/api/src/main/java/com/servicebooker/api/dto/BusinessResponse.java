package com.servicebooker.api.dto;

import java.util.UUID;

public record BusinessResponse(
        UUID id,
        String name,
        String timezone,
        String phone,
        String email,
        UpsertBusinessRequest.AddressDto address,
        boolean active
) {
}
