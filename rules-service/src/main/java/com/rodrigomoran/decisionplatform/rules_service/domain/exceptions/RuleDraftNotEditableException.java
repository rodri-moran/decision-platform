package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class RuleDraftNotEditableException extends RuntimeException {
    public RuleDraftNotEditableException() {
        super("Rule draft not editable");
    }
}
