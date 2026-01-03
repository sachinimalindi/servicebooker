package com.servicebooker.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCustomerRequest(
        @NotNull UUID businessId,
        @NotBlank String fullName,
        @Email @NotBlank String email,
        String phone
) {
}
