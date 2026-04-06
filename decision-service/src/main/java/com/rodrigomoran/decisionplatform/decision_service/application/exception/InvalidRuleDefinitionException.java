package com.rodrigomoran.decisionplatform.decision_service.application.exception;

public class InvalidRuleDefinitionException extends RuntimeException {
    public InvalidRuleDefinitionException(String message) {
        super(message);
    }
}
