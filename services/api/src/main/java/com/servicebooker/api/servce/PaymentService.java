package com.servicebooker.api.servce;

import com.servicebooker.api.common.NotFoundException;
import com.servicebooker.api.dto.CreatePaymentAttemptRequest;
import com.servicebooker.api.dto.PaymentAttemptResponse;
import com.servicebooker.api.model.PaymentAttempt;
import com.servicebooker.api.model.common.Money;
import com.servicebooker.api.repository.BookingRepository;
import com.servicebooker.api.repository.PaymentAttemptRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentAttemptRepository paymentAttemptRepository;
    private final BookingRepository bookingRepository;

    public PaymentAttemptResponse createPayment( CreatePaymentAttemptRequest request) {

        bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> NotFoundException.of("Booking", request.bookingId().toString()));

        var attempt = PaymentAttempt.createNew(
                request.bookingId(),
                new Money(request.amount(), request.currency()),
                PaymentAttempt.PaymentProvider.valueOf(request.provider().toUpperCase()),
                request.externalRef()
        );

        return getPaymentAttemptResponse(paymentAttemptRepository.save(attempt));
    }

    @Transactional(readOnly = true)
    public List<PaymentAttemptResponse> listOfPaymentsByBooking(UUID bookingId) {
        return paymentAttemptRepository.findByBookingId(bookingId).stream().map(this::getPaymentAttemptResponse).toList();
    }

    public PaymentAttemptResponse markPaymentSucceeded(UUID id) {
        var attempt = paymentAttemptRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("PaymentAttempt", id.toString()));
        attempt.markSucceeded();
        return getPaymentAttemptResponse(paymentAttemptRepository.save(attempt));
    }

    public PaymentAttemptResponse markPaymentFailed(UUID id, String reason) {
        var attempt = paymentAttemptRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("PaymentAttempt", id.toString()));
        attempt.markFailed(reason);
        return getPaymentAttemptResponse(paymentAttemptRepository.save(attempt));
    }

    private PaymentAttemptResponse getPaymentAttemptResponse(PaymentAttempt paymentAttempt) {
        return new PaymentAttemptResponse(  paymentAttempt.getId(),
                paymentAttempt.getBookingId(),
                paymentAttempt.getAmount().getAmount(),
                paymentAttempt.getAmount().getCurrency(),
                paymentAttempt.getProvider().name(),
                paymentAttempt.getStatus().name(),
                paymentAttempt.getExternalRef(),
                paymentAttempt.getFailureReason());
    }

}
