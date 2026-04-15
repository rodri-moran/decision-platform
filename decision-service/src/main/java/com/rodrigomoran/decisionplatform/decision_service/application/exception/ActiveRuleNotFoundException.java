package com.rodrigomoran.decisionplatform.decision_service.application.exception;

public class ActiveRuleNotFoundException extends RuntimeException {
    public ActiveRuleNotFoundException(String ruleKey) {
        super("No active rule found for ruleKey: " + ruleKey);
    }
}
