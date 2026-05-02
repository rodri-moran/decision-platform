package com.rodrigomoran.decisionplatform.audit_service.application.dto;

import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditAggregateType;
import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditEventMessage {
    private String eventId;
    private String traceId;
    private Instant occurredAt;
    private AuditEventType eventType;
    private AuditAggregateType aggregateType;
    private String aggregateId;
}
