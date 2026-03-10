package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class InvalidJsonException extends RuntimeException {
    public InvalidJsonException(String name) {
        super(name + " is not a valid JSON");
    }
}