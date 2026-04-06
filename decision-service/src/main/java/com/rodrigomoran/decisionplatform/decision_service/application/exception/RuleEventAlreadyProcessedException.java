package com.rodrigomoran.decisionplatform.decision_service.application.exception;

public class RuleEventAlreadyProcessedException extends RuntimeException {
    public RuleEventAlreadyProcessedException(String message) {
        super(message);
    }
}
