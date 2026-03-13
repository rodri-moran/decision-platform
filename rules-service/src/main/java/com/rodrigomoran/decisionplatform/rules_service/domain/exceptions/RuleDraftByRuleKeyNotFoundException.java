package com.rodrigomoran.decisionplatform.rules_service.domain.exceptions;

public class RuleDraftByRuleKeyNotFoundException extends RuntimeException {
  public RuleDraftByRuleKeyNotFoundException(String ruleKey) {
        super("Rule draft with rulekey: " + ruleKey + " not found.");
    }

}
