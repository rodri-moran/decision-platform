package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;
public class RuleKeyAlreadyExistsException extends RuntimeException{
    public RuleKeyAlreadyExistsException(String ruleKey) {
        super("A rule with key '" + ruleKey + "' already exists'");
    }
}