package com.servicebooker.api.model;

import com.servicebooker.api.model.common.BaseEntity;
import com.servicebooker.api.model.common.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "payment_attempts",
        indexes = @Index(name="idx_payment_booking", columnList = "bookingId"))
public class PaymentAttempt extends BaseEntity {



    public enum PaymentStatus { INITIATED, SUCCEEDED, FAILED, REFUNDED }
    public enum PaymentProvider { STRIPE, PAYPAL, MANUAL }

    @Column(nullable = false)
    private UUID bookingId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="amount", nullable=false)),
            @AttributeOverride(name="currency", column=@Column(name="currency", nullable=false))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.INITIATED;

    private String externalRef;

    @Column(length = 2000)
    private String failureReason;

    protected PaymentAttempt() {}

    public PaymentAttempt(UUID bookingId, Money amount, PaymentProvider provider, String externalRef) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.provider = provider;
        this.externalRef = externalRef;
    }

    public UUID getBookingId() { return bookingId; }
    public Money getAmount() { return amount; }
    public PaymentProvider getProvider() { return provider; }
    public PaymentStatus getStatus() { return status; }
    public String getExternalRef() { return externalRef; }

    public String getFailureReason() {
        return failureReason;
    }

    public void markSucceeded() { this.status = PaymentStatus.SUCCEEDED; this.failureReason = null; }
    public void markFailed(String reason) { this.status = PaymentStatus.FAILED; this.failureReason = reason; }

    public static PaymentAttempt createNew( UUID bookingId, Money money, PaymentProvider paymentProvider, String externalRef) {
        return new PaymentAttempt(bookingId, money, paymentProvider, externalRef);
    }

}
