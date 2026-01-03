package com.servicebooker.api.model;

import com.servicebooker.api.model.common.BaseEntity;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings",
        indexes = {
                @Index(name="idx_booking_business_start", columnList = "businessId,startAt"),
                @Index(name="idx_booking_customer", columnList = "customerId")
        })
public class Booking extends BaseEntity {

    public enum BookingStatus { PENDING, CONFIRMED, CANCELLED, COMPLETED }

    @Column(nullable = false)
    private UUID businessId;

    @Column(nullable = false)
    private UUID serviceId;

    @Column(nullable = false)
    private UUID customerId;

    @Column(name="startAt", nullable = false)
    private ZonedDateTime start;

    @Column(name="endAt", nullable = false)
    private ZonedDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(length = 2000)
    private String customerNotes;

    @Column(length = 2000)
    private String internalNotes;

    protected Booking() {}

    public Booking(UUID businessId, UUID serviceId, UUID customerId, ZonedDateTime start, ZonedDateTime end, String customerNotes) {
        this.businessId = businessId;
        this.serviceId = serviceId;
        this.customerId = customerId;
        this.start = start;
        this.end = end;
        this.customerNotes = customerNotes;
    }

    public UUID getBusinessId() { return businessId; }
    public UUID getServiceId() { return serviceId; }
    public UUID getCustomerId() { return customerId; }
    public ZonedDateTime getStart() { return start; }
    public ZonedDateTime getEnd() { return end; }
    public BookingStatus getStatus() { return status; }
    public String getCustomerNotes() {return customerNotes;}

    public void confirm() { this.status = BookingStatus.CONFIRMED; }
    public void cancel(String reason) { this.status = BookingStatus.CANCELLED; this.internalNotes = reason; }
    public void reschedule(ZonedDateTime newStart, ZonedDateTime newEnd) { this.start = newStart; this.end = newEnd; }
}
