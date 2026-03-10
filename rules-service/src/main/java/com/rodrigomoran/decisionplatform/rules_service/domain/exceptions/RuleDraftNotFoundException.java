package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class RuleDraftNotFoundException extends RuntimeException {
    public RuleDraftNotFoundException(Long id) {
        super("Rule draft with id " + id + " not found.");
    }
}
