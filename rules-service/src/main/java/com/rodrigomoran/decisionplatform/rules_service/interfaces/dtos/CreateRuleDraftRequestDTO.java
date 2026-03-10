package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;

import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CreateRuleDraftRequestDTO(@NotNull @NotBlank
                                        String ruleKey,
                                        @NotNull @NotBlank
                                        String name,
                                        String description,
                                        @NotNull @NotBlank
                                        ParsedRuleDefinition definitionJson,
                                        Map<String, Object> metadataJson) {
}