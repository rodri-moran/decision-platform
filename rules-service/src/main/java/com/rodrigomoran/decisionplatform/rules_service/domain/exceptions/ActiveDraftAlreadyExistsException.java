package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class ActiveDraftAlreadyExistsException extends RuntimeException {
    public ActiveDraftAlreadyExistsException(String ruleKey) {
        super("A draft already exists for rule key '" + ruleKey + "'");
    }
}
