package com.rodrigomoran.decisionplatform.audit_service.application.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditSearchRequest {
    private String eventType;
    private String aggregateType;
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
