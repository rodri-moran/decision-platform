package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class IdempotencyKeyReusedWithDifferentPayloadException extends RuntimeException {
    public IdempotencyKeyReusedWithDifferentPayloadException(String message) {
        super(message);
    }
}
