package com.rodrigomoran.decisionplatform.rules_service.application.simulation.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rodrigomoran.decisionplatform.rules_service.domain.model.RuleConditionGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedRuleDefinition {
    private String operator;
    private List<RuleCondition> conditions;
    private List<RuleConditionGroup> groups;
}