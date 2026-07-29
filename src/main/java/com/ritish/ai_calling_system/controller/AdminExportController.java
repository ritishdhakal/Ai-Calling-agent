package com.ritish.ai_calling_system.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ritish.ai_calling_system.entity.Appointment;
import com.ritish.ai_calling_system.service.BookingService;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController

public class AdminExportController {
    private final BookingService bookingService;

    public AdminExportController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/api/admin/appointments/export.csv")
    public ResponseEntity<String> exportAppointmentsCsv() {
        List<Appointment> appointments = bookingService.listAppointments();

        StringBuilder csv = new StringBuilder();

        csv.append("AppointmentId , CustomerName, Phone, ServiceType, StartTime, Status \n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Appointment a : appointments) {
            String line = String.format("%d , %s, %s,%s,%s,%s\n",

                    a.getId(),
                    safe(a.getCustomer().getName()),
                    safe(a.getCustomer().getPhone()),
                    safe(a.getServiceType()),
                    a.getStartTime().format(formatter),
                    safe(a.getStatus())

            );
            csv.append(line);

        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=appointments.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.toString());

    }

    private String safe(String value) {
        if (value == null)
            return "";
        // Wrap in quotes to keep commas safe for Excel
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

}
