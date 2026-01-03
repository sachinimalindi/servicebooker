package com.servicebooker.api.controller;


import com.servicebooker.api.dto.CreateServiceRequest;
import com.servicebooker.api.dto.UpdateServiceRequest;
import com.servicebooker.api.dto.common.ApiResponse;
import com.servicebooker.api.dto.common.ServiceResponse;
import com.servicebooker.api.servce.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ApiResponse<List<ServiceResponse>> listActive(@RequestParam UUID businessId) {
        var results = serviceService.listActiveServices(businessId);
        return ApiResponse.of(results);
    }

    @PostMapping
    public ApiResponse<ServiceResponse> create(@Valid @RequestBody CreateServiceRequest request) {
        var result = serviceService.createService(request);
        return ApiResponse.of(result);
    }

    @PutMapping("/{id}")
    public ApiResponse<ServiceResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateServiceRequest request) {
        var result = serviceService.updateService(id, request);
        return ApiResponse.of(result);
    }
}
