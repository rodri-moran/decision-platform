package com.rodrigomoran.decisionplatform.audit_service.application.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RulePublishedAuditMessage extends AuditEventMessage {
    private String ruleKey;
    private Integer version;
    private String actor;
    private Instant publishedAt;
}