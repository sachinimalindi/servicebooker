package com.servicebooker.api.controller;


import com.servicebooker.api.dto.CreatePaymentAttemptRequest;
import com.servicebooker.api.dto.PaymentAttemptResponse;
import com.servicebooker.api.dto.common.ApiResponse;
import com.servicebooker.api.servce.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/attempts")
    public ApiResponse<PaymentAttemptResponse> createAttempt(@Valid @RequestBody CreatePaymentAttemptRequest request) {
        var result = paymentService.createPayment(request);
        return ApiResponse.of(result);
    }

    @GetMapping("/attempts")
    public ApiResponse<List<PaymentAttemptResponse>> listAttempts(@RequestParam UUID bookingId) {
        var results = paymentService.listOfPaymentsByBooking(bookingId).stream().toList();
        return ApiResponse.of(results);
    }

    @PostMapping("/attempts/{id}/succeeded")
    public ApiResponse<PaymentAttemptResponse> markSucceeded(@PathVariable UUID id) {
        var result = paymentService.markPaymentSucceeded(id);
        return ApiResponse.of(result);
    }

    @PostMapping("/attempts/{id}/failed")
    public ApiResponse<PaymentAttemptResponse> markFailed(@PathVariable UUID id, @RequestParam String reason) {
        var result = paymentService.markPaymentFailed(id, reason);
        return ApiResponse.of(result);
    }

}
