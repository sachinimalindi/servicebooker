package com.servicebooker.api.dto;

import java.time.ZonedDateTime;

public record SlotResponse(
        ZonedDateTime startAt,
        ZonedDateTime endAt
) {
}
