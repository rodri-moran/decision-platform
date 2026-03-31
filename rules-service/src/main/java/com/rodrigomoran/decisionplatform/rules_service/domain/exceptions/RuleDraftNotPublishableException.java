package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class RuleDraftNotPublishableException extends RuntimeException {
    public RuleDraftNotPublishableException() {
        super("Rule draft cannot be published in its current state");
    }
}