package com.rodrigomoran.decisionplatform.audit_service.domain.model;

import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditAggregateType;
import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditEventType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditSearchCriteria {
    private AuditEventType eventType;
    private AuditAggregateType aggregateType;
    private String aggregateId;
    private String traceId;
    private String decision;
    private Instant fromTimestamp;
    private Instant toTimestamp;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}
