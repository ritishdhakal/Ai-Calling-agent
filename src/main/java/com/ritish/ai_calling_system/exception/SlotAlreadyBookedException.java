package com.ritish.ai_calling_system.exception;

public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
