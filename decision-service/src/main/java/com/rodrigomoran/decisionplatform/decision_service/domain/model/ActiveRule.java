package com.rodrigomoran.decisionplatform.decision_service.domain.model;
import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveRule {
    private Long id;
    private String ruleKey;
    private Integer version;
    private String name;
    private String description;
    private String definitionJson;
    private String metadataJson;
    private Instant publishedAt;
    private String publishedBy;
    private String traceId;
    private Instant createdAt;
    private Instant updatedAt;
}