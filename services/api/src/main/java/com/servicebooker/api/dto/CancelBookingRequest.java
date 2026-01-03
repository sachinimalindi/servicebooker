package com.servicebooker.api.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CancelBookingRequest(
       @Size(max = 2000) String reason
) {
}
