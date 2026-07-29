package com.ritish.ai_calling_system.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ritish.ai_calling_system.dto.ApiError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream().map(this::formatFieldError).toList();

        ApiError body = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messages,
                request.getRequestURI());

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                List.of(ex.getMessage()),
                request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)

    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                List.of(ex.getMessage()),
                request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)

    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request

    ) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                List.of("Something went wrong. Please try again"),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<ApiError> handleSlotAlreadyBooked(SlotAlreadyBookedException ex, HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                List.of(ex.getMessage()),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                List.of(ex.getMessage()),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    private String formatFieldError(FieldError e) {
        return e.getField() + ": " + e.getDefaultMessage();

    }

}
