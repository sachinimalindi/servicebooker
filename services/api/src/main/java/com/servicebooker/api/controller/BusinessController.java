package com.servicebooker.api.controller;

import com.servicebooker.api.dto.BusinessResponse;
import com.servicebooker.api.dto.UpsertBusinessRequest;
import com.servicebooker.api.dto.common.ApiResponse;
import com.servicebooker.api.servce.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/{id}")
    public ApiResponse<BusinessResponse> get(@PathVariable UUID id) {
        var result = businessService.getBusiness(id);
        return ApiResponse.of(result);
    }

    @PutMapping("/{id}")
    public ApiResponse<BusinessResponse> update(@PathVariable UUID id, @Valid @RequestBody UpsertBusinessRequest request) {
        var result = businessService.updateBusiness(id, request);
        return ApiResponse.of(result);
    }


}
