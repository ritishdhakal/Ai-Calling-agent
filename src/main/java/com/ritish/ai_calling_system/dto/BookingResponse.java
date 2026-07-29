package com.ritish.ai_calling_system.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,
        String customerName,
        String phone,
        String serviceType,
        LocalDateTime startTime,
        String status

) {
}
