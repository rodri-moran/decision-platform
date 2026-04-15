package com.rodrigomoran.decisionplatform.rules_service.application.simulation.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleCondition {

    private String field;
    private String operator;
    private Object value;
}