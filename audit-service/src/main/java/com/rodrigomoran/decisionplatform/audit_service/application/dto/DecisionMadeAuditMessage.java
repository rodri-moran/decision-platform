package com.rodrigomoran.decisionplatform.audit_service.application.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DecisionMadeAuditMessage extends AuditEventMessage {
    private String decisionId;
    private String ruleKey;
    private Integer ruleVersion;
    private String decision;
    private String matchedRule;
    private List<String> reasons;
    private String inputRedactedJson;
    private Long latencyMs;
    private String actor;
}