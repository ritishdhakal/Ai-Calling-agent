package com.ritish.ai_calling_system.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ritish.ai_calling_system.dto.BookingRequest;
import com.ritish.ai_calling_system.entity.Appointment;
import com.ritish.ai_calling_system.entity.Customer;
import com.ritish.ai_calling_system.exception.ResourceNotFoundException;
import com.ritish.ai_calling_system.exception.SlotAlreadyBookedException;
import com.ritish.ai_calling_system.repository.AppointmentRepository;
import com.ritish.ai_calling_system.repository.CustomerRepository;

@Service
public class BookingService {

    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(18, 0);
    private static final int SLOT_MINUTES = 30;

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;

    public BookingService(CustomerRepository customerRepository, AppointmentRepository appointmentRepository)

    {
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;

    }

    @Transactional // this means all database operation inside this method should succeed together.
                   // If something fails, undo the changes

    public Appointment createBooking(BookingRequest request) {

        LocalDateTime start = request.startTime();
        validateBusinessRules(start);

        boolean slotTaken = appointmentRepository.existsByStartTimeAndStatus(request.startTime(), "BOOKED");
        if (slotTaken) {
            throw new SlotAlreadyBookedException("This slot is already booked");
        }

        Customer customer = customerRepository.findByPhone(request.phone())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setName(request.name());
                    c.setPhone(request.phone());
                    c.setTier("STANDARD");
                    return customerRepository.save(c);
                });
        Appointment appt = new Appointment();
        appt.setCustomer(customer);
        appt.setServiceType(request.serviceType());
        appt.setStartTime(request.startTime());
        appt.setStatus("BOOKED");

        return appointmentRepository.save(appt);
    }

    private void validateBusinessRules(LocalDateTime start)
    // no sunday booking
    {
        if (start.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Bookigns are not available on sunday");

        }
        LocalTime time = start.toLocalTime();
        if (time.isBefore(OPEN_TIME) || !time.isBefore(CLOSE_TIME)) {
            throw new IllegalArgumentException("Booking time must be between 09:00 and 18:00.");
        }
        if (time.getMinute() % SLOT_MINUTES != 0 || time.getSecond() != 0 || time.getNano() != 0) {
            throw new IllegalArgumentException("Booking time must be on 30-minute slots (e.g., 10:00, 10:30).");
        }
    }

    @Transactional(readOnly = true)
    public List<Appointment> listAppointments() {
        return appointmentRepository.findByOrderByStartTimeAsc();
    }

    @Transactional(readOnly = true)
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id : " + id));
    }

    @Transactional
    public Appointment cancelBooking(Long id) {
        Appointment appt = getAppointmentById(id);

        if ("CANCELLED".equalsIgnoreCase(appt.getStatus())) {
            throw new IllegalArgumentException("Booking is already cancelled");

        }
        appt.setStatus("CANCELLED");
        return appointmentRepository.save(appt);

    }

    @Transactional
    public Appointment rescheduleBooking(Long id, LocalDateTime newStarTime) {
        Appointment appt = getAppointmentById(id);
        if ("CANCELLED".equalsIgnoreCase(appt.getStatus())) {
            throw new IllegalArgumentException("Cancelled booking cannot be rescheduled");

        }
        validateBusinessRules(newStarTime);

        boolean slotTaken = appointmentRepository.existsByStartTimeAndStatusAndIdNot(newStarTime, "BOOKED", id);

        if (slotTaken) {
            throw new SlotAlreadyBookedException("This slot is already booked");

        }
        appt.setStartTime(newStarTime);
        appt.setStatus("BOOKED");
        return appointmentRepository.save(appt);
    }

}
