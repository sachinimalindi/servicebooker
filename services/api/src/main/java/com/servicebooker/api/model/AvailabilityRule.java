package com.servicebooker.api.model;


import com.servicebooker.api.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "availability_rules",
        indexes = @Index(name="idx_avail_business_day", columnList = "businessId,dayOfWeek"))
public class AvailabilityRule extends BaseEntity {

    @Column(nullable = false)
    private UUID businessId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int bufferMinutesBefore = 0;

    @Column(nullable = false)
    private int bufferMinutesAfter = 0;

    @Setter
    @Column(nullable = false)
    private boolean active = true;

    protected AvailabilityRule() {}

    public AvailabilityRule(UUID businessId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime,
                            int bufferMinutesBefore, int bufferMinutesAfter) {
        this.businessId = businessId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bufferMinutesBefore = bufferMinutesBefore;
        this.bufferMinutesAfter = bufferMinutesAfter;
    }

    public UUID getBusinessId() { return businessId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getBufferMinutesBefore() { return bufferMinutesBefore; }
    public int getBufferMinutesAfter() { return bufferMinutesAfter; }
    public boolean isActive() { return active; }

}
