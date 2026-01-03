package com.servicebooker.api.controller;

import com.servicebooker.api.dto.BookingResponse;
import com.servicebooker.api.dto.CancelBookingRequest;
import com.servicebooker.api.dto.CreateBookingRequest;
import com.servicebooker.api.dto.RescheduleBookingRequest;
import com.servicebooker.api.dto.common.ApiResponse;
import com.servicebooker.api.dto.common.PageMeta;
import com.servicebooker.api.servce.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingResponse> create(@Valid @RequestBody CreateBookingRequest request) {
        var result = bookingService.createBooking(request);
        return ApiResponse.of(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> get(@PathVariable UUID id) {
        var result = bookingService.getBooking(id);
        return ApiResponse.of(result);
    }

    @PatchMapping("/{id}/reschedule")
    public ApiResponse<BookingResponse> reschedule(@PathVariable UUID id, @Valid @RequestBody RescheduleBookingRequest request) {
        var result = bookingService.rescheduleBooking(id, request);
        return ApiResponse.of(result);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancel(@PathVariable UUID id, @Valid @RequestBody CancelBookingRequest request) {
        var result = bookingService.cancelBooking(id, request);
        return ApiResponse.of(result);
    }

    @GetMapping
    public ApiResponse<List<BookingResponse>> listByBusiness(
            @RequestParam UUID businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startAt,asc") String sort
    ) {
        var pageable = PageRequest.of(page, size, parseSort(sort));
        var resultPage = bookingService.listByBusiness(businessId, pageable);
        var data = resultPage.stream().toList();
        var meta = new PageMeta(resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalElements(), resultPage.getTotalPages());
        return ApiResponse.of(data, meta);
    }

    private Sort parseSort(String sort) {
        // "field,asc" or "field,desc"
        var parts = sort.split(",", 2);
        var field = parts[0];
        var dir = (parts.length == 2 && "desc".equalsIgnoreCase(parts[1])) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }
}
