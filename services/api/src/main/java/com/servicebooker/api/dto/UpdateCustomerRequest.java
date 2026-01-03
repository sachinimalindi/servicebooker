package com.servicebooker.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequest(
        @NotBlank String fullName,
        String phone
) {
}
