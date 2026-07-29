package com.ritish.ai_calling_system.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BookingRequest(
        @NotBlank(message = "Name is Required") String name,
        @NotBlank(message = " Phine is required")

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits") String phone,

        @NotBlank(message = "Service type is required")

        @Pattern(regexp = "^(OIL_CHANGE|GENERAL_SERVICE|BRAKE_INSPECTION|ENGINE_DIAGNOSTIC)$", message = "Service type must be one of: OIL_CHANGE, GENERAL_SERVICE, BRAKE_INSPECTION, ENGINE_DIAGNOSTIC")

        String serviceType,

        @NotNull(message = "start time is required") @FutureOrPresent(message = "Start time must be in future or present") LocalDateTime startTime) {
}
