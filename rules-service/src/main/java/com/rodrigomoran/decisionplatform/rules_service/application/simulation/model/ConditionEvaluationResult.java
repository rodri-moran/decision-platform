package com.rodrigomoran.decisionplatform.rules_service.application.simulation.model;

public class ConditionEvaluationResult {

    private final boolean matched;
    private final String reason;

    public ConditionEvaluationResult(boolean matched, String reason) {
        this.matched = matched;
        this.reason = reason;
    }

    public boolean isMatched() {
        return matched;
    }

    public String getReason() {
        return reason;
    }
}
