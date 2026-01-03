package com.servicebooker.api.servce;

import com.servicebooker.api.common.ConflictException;
import com.servicebooker.api.common.NotFoundException;
import com.servicebooker.api.dto.BookingResponse;
import com.servicebooker.api.dto.CancelBookingRequest;
import com.servicebooker.api.dto.CreateBookingRequest;
import com.servicebooker.api.dto.RescheduleBookingRequest;
import com.servicebooker.api.model.Booking;
import com.servicebooker.api.repository.BookingRepository;
import com.servicebooker.api.repository.BusinessRepository;
import com.servicebooker.api.repository.CustomerRepository;
import com.servicebooker.api.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BusinessRepository businessRepository;
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;


    public BookingResponse createBooking(CreateBookingRequest request) {

        businessRepository.findByIdAndActiveTrue(request.businessId())
                .orElseThrow(() -> NotFoundException.of("Business", request.businessId().toString()));
        serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> NotFoundException.of("Service", request.serviceId().toString()));
        customerRepository.findById(request.customerId())
                .orElseThrow(() -> NotFoundException.of("Customer", request.customerId().toString()));

        boolean overlaps = bookingRepository.existsOverlapping(
                request.businessId(), request.startAt(), request.endAt(), Booking.BookingStatus.CANCELLED
        );
        if (overlaps) throw new ConflictException("Requested time overlaps with an existing booking");

        Booking booking = new Booking(request.businessId(), request.serviceId(), request.customerId(), request.startAt(), request.endAt(), request.customerNotes());
        booking = bookingRepository.save(booking);
       return getBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID id) {
    var booking =  bookingRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Booking", id.toString()));
        return getBookingResponse(booking);

    }

    @Transactional(readOnly = true)
    public Page<BookingResponse> listByBusiness(UUID businessId, PageRequest pageable) {
        Page<Booking> bookings = bookingRepository.findByBusinessId(businessId, pageable);
        return bookings.map(this::getBookingResponse);
    }


    public BookingResponse rescheduleBooking(UUID id, RescheduleBookingRequest request) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Booking", id.toString()));

        boolean overlaps = bookingRepository.existsOverlapping(
                id, request.startAt(), request.endAt(), Booking.BookingStatus.CANCELLED
        );
        if (overlaps) throw new ConflictException("Requested time overlaps with an existing booking");

        booking.reschedule(request.startAt(), request.endAt());
        return getBookingResponse(bookingRepository.save(booking));
    }

    public BookingResponse cancelBooking(UUID id, CancelBookingRequest request) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Booking", id.toString()));

        booking.cancel(request.reason());
        return getBookingResponse(bookingRepository.save(booking));
    }

    private BookingResponse getBookingResponse(Booking booking) {
        return new BookingResponse(booking.getId(), booking.getBusinessId(), booking.getServiceId(), booking.getCustomerId(), booking.getStart(), booking.getEnd(), booking.getStatus().name(), booking.getCustomerNotes());
    }
}
