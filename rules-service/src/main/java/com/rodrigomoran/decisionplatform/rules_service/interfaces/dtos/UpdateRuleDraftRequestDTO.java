package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;

import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;

import java.util.Map;

public record UpdateRuleDraftRequestDTO(
                                        String name,
                                        String description,
                                        ParsedRuleDefinition definitionJson,
                                        Map<String, Object> metadataJson) {
}