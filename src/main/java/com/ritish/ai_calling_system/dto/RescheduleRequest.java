package com.ritish.ai_calling_system.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record RescheduleRequest(

        @NotNull(message = "start Time is required") @FutureOrPresent(message = "starttime must be in present or the future") LocalDateTime starTime) {
}