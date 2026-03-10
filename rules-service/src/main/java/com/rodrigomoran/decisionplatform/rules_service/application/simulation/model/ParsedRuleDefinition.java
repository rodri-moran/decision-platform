package com.rodrigomoran.decisionplatform.rules_service.application.simulation.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedRuleDefinition {

    private String logic = "AND";
    private List<RuleCondition> when;
    private RuleAction then;

    public ParsedRuleDefinition() {
    }

    public ParsedRuleDefinition(String logic, List<RuleCondition> when, RuleAction then) {
        this.logic = logic;
        this.when = when;
        this.then = then;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public List<RuleCondition> getWhen() {
        return when;
    }

    public void setWhen(List<RuleCondition> when) {
        this.when = when;
    }

    public RuleAction getThen() {
        return then;
    }

    public void setThen(RuleAction then) {
        this.then = then;
    }
}