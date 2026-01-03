package com.servicebooker.api.servce;

import com.servicebooker.api.dto.SetAvailabilityRequest;
import com.servicebooker.api.dto.SlotQueryParams;
import com.servicebooker.api.dto.SlotResponse;
import com.servicebooker.api.model.AvailabilityRule;
import com.servicebooker.api.model.Booking;
import com.servicebooker.api.repository.AvailabilityRepository;
import com.servicebooker.api.repository.BookingRepository;
import com.servicebooker.api.repository.BusinessRepository;
import com.servicebooker.api.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;
    private final BusinessRepository businessRepository;
    private final ServiceRepository serviceRepository;

    public void setRules( SetAvailabilityRequest request) {
        var businessId = request.businessId();


        businessRepository.findByIdAndActiveTrue(businessId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Business not Found: "+businessId));

        availabilityRepository.deleteAllByBusinessId(businessId);

        for (var r : request.rules()) {
            var rule = new AvailabilityRule (
                    businessId,
                    r.dayOfWeek(),
                    r.startTime(),
                    r.endTime(),
                    r.bufferMinutesBefore(),
                    r.bufferMinutesAfter()
            );
            // apply active flag (createNew defaults true)
            if (!r.active()) {
                // simplest: rehydrate as inactive
                rule.setActive(false);
            }
            availabilityRepository.save(rule);
        }
    }

    @Transactional(readOnly = true)
    public List<SlotResponse> getSlots( SlotQueryParams params) {
        var businessId = params.businessId();
        var serviceId = params.serviceId();

        var business = businessRepository.findByIdAndActiveTrue(businessId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Business not Found: "+businessId));

        var service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Service not Found: "+serviceId));

        var zone = ZoneId.of(business.getTimezone());
        var date = params.date();
        var day = date.getDayOfWeek();

        var rules = availabilityRepository.findByBusinessIdAndActiveTrue(businessId).stream()
                .filter(r -> r.isActive() && r.getDayOfWeek() == day)
                .toList();

        if (rules.isEmpty()) return List.of();

        // Bookings for that day (simple: fetch a page and filter in memory for MVP)
        // Production: add a repository query by businessId and date range.
        var startOfDay = date.atStartOfDay(zone);
        var endOfDay = date.plusDays(1).atStartOfDay(zone);
        var bookings = bookingRepository.findByBusinessId(businessId, PageRequest.of(0, 500, Sort.by("startAt").ascending()))
                .getContent().stream()
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED)
                .filter(b -> b.getStart().isBefore(endOfDay) && b.getEnd().isAfter(startOfDay))
                .collect(Collectors.toList());

        int durationMin = service.getDurationMinutes();
        int stepMin = 15; // slot granularity

        List<SlotResponse> out = new ArrayList<>();

        for (var rule : rules) {
            var windowStart = ZonedDateTime.of(date, rule.getStartTime(), zone);
            var windowEnd = ZonedDateTime.of(date, rule.getEndTime(), zone);

            // apply buffers as "unavailable" at edges (simple)
            windowStart = windowStart.plusMinutes(rule.getBufferMinutesBefore());
            windowEnd = windowEnd.minusMinutes(rule.getBufferMinutesAfter());
            if (!windowEnd.isAfter(windowStart)) continue;

            for (var cursor = windowStart; cursor.plusMinutes(durationMin).compareTo(windowEnd) <= 0; cursor = cursor.plusMinutes(stepMin)) {
                var slotStart = cursor;
                var slotEnd = cursor.plusMinutes(durationMin);

                boolean overlaps = overlapsAny(slotStart, slotEnd, bookings);
                if (!overlaps) out.add(new SlotResponse(slotStart, slotEnd));
            }
        }

        out.sort(Comparator.comparing(SlotResponse::startAt));
        return out;
    }

    private boolean overlapsAny(ZonedDateTime start, ZonedDateTime end, List<Booking> bookings) {
        for (var b : bookings) {
            if (b.getStart().isBefore(end) && b.getEnd().isAfter(start)) return true;
        }
        return false;
    }


}
