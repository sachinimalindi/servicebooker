package com.servicebooker.api.dto.common;

import java.util.List;

public record ErrorResponse(String code, String message, List<ErrorItem> errors) {
}
