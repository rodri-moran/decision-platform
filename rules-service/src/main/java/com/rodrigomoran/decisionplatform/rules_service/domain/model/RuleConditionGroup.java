package com.rodrigomoran.decisionplatform.rules_service.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.RuleCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleConditionGroup {
    private String operator;
    private List<RuleCondition> conditions = new ArrayList<>();
    private List<RuleConditionGroup> groups = new ArrayList<>();
}
