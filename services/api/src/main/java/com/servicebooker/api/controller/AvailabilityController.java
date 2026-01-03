package com.servicebooker.api.controller;

import com.servicebooker.api.dto.SetAvailabilityRequest;
import com.servicebooker.api.dto.SlotQueryParams;
import com.servicebooker.api.dto.SlotResponse;
import com.servicebooker.api.dto.common.ApiResponse;
import com.servicebooker.api.servce.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @PutMapping("/availability")
    public ApiResponse<Void> setAvailability(@Valid @RequestBody SetAvailabilityRequest request) {
        availabilityService.setRules(request);
        return ApiResponse.of(null);
    }

    @GetMapping("/slots")
    public ApiResponse<List<SlotResponse>> getSlots(@Valid SlotQueryParams params) {
        // Binding: businessId/serviceId/date as query params
        var results = availabilityService.getSlots(params)
                .stream().toList();
        return ApiResponse.of(results);
    }


}
