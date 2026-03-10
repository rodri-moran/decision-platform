package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;

import com.rodrigomoran.decisionplatform.rules_service.domain.enums.ChangeType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;

import java.time.Instant;

public record RuleVersionResponseDTO(Long id,
                                     String ruleKey,
                                     Integer versionNumber,
                                     RuleVersionStatus status,
                                     String publishedBy,
                                     Instant publishedAt,
                                     String metadataJson,
                                     ChangeType changeType,
                                     String definitionJson,
                                     String traceId
) {
}
