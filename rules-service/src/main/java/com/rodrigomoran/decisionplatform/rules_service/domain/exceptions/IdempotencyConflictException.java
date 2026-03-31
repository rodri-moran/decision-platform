package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class IdempotencyConflictException extends RuntimeException {
    public IdempotencyConflictException(String message) {
        super(message);
    }
}
