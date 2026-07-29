package com.ritish.ai_calling_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ritish.ai_calling_system.dto.BookingRequest;
import com.ritish.ai_calling_system.dto.BookingResponse;
import com.ritish.ai_calling_system.dto.RescheduleRequest;
import com.ritish.ai_calling_system.entity.Appointment;
import com.ritish.ai_calling_system.service.BookingService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;

    }

    @PostMapping
    // @Valid checks dto validation rule
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        Appointment saved = bookingService.createBooking(request);

        return toResponse(saved);
    }

    @GetMapping
    public List<BookingResponse> list() {
        return bookingService.listAppointments()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        Appointment appt = bookingService.getAppointmentById(id);
        return toResponse(appt);
    }

    @PatchMapping("/{id}/cancel")

    public BookingResponse cancel(@PathVariable Long id) {
        Appointment appt = bookingService.cancelBooking(id);
        return toResponse(appt);

    }

    @PatchMapping("/{id}/reschedule")

    public BookingResponse reschedule(@PathVariable Long id, @Valid @RequestBody RescheduleRequest request) {
        Appointment appt = bookingService.rescheduleBooking(id, request.starTime());
        return toResponse(appt);
    }

    private BookingResponse toResponse(Appointment saved) {
        return new BookingResponse(
                saved.getId(),
                saved.getCustomer().getName(),
                saved.getCustomer().getPhone(),
                saved.getServiceType(),
                saved.getStartTime(),
                saved.getStatus());
    }

}
