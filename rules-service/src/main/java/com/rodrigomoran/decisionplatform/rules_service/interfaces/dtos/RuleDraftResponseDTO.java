package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import java.time.Instant;
import java.util.Map;

public record RuleDraftResponseDTO(Long id,
                                   String ruleKey,
                                   String name,
                                   String description,
                                   ParsedRuleDefinition definitionJson,
                                   Map<String, Object> metadataJson,
                                   RuleDraftStatus status,
                                   String createdBy,
                                   String updatedBy,
                                   Instant createdAt,
                                   Instant updatedAt,
                                   String traceId

) {
}