package com.servicebooker.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpsertBusinessRequest(
        @NotBlank String name,
        @NotBlank String timezone,
        String phone,
        @Email String email,
        AddressDto address,
        boolean active
) {
    public record AddressDto(
            @NotBlank String line1,
            String line2,
            @NotBlank String suburb,
            @NotBlank String state,
            @NotBlank String postcode,
            @NotBlank String country
    ) {}
}
