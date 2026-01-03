package com.servicebooker.api.controller;

import com.servicebooker.api.dto.CreateCustomerRequest;
import com.servicebooker.api.dto.CustomerResponse;
import com.servicebooker.api.dto.UpdateCustomerRequest;
import com.servicebooker.api.dto.common.ApiResponse;
import com.servicebooker.api.dto.common.PageMeta;
import com.servicebooker.api.servce.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        var result = customerService.createCustomer(request);
        return ApiResponse.of(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerResponse> get(@PathVariable UUID id) {
        var result = customerService.getCustomer(id);
        return ApiResponse.of(result);
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateCustomerRequest request) {
        var result = customerService.updateCustomer(id, request);
        return ApiResponse.of(result);
    }

    @GetMapping
    public ApiResponse<java.util.List<CustomerResponse>> listByBusiness(
            @RequestParam UUID businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var pageable = PageRequest.of(page, size, Sort.by("email").ascending());
        var resultPage = customerService.listOfCustomersByBusiness(businessId, pageable);
        var data = resultPage.getContent().stream().toList();
        var meta = new PageMeta(resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalElements(), resultPage.getTotalPages());
        return ApiResponse.of(data, meta);
    }
}
