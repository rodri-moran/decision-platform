package com.rodrigomoran.decisionplatform.decision_service.application.exception;

public class RuleEventAlreadyProcessedException extends RuntimeException {
    public RuleEventAlreadyProcessedException(String eventId) {
        super("Rule event already processed: " + eventId);
    }
}
