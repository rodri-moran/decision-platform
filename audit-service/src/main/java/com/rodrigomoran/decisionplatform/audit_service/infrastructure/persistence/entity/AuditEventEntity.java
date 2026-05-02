package com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity;

import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditAggregateType;
import com.rodrigomoran.decisionplatform.audit_service.domain.enums.AuditEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_events",
        indexes = {
                @Index(name = "idx_audit_event_event_id", columnList = "event_id", unique = true),
                @Index(name = "idx_audit_event_trace_id", columnList = "trace_id"),
                @Index(name = "idx_audit_event_aggregate_id", columnList = "aggregate_id"),
                @Index(name = "idx_audit_event_event_type", columnList = "event_type"),
                @Index(name = "idx_audit_event_occurred_at", columnList = "occurred_at")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_id", nullable = false, unique = true, length = 100)
    private String eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private AuditEventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private AuditAggregateType aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 150)
    private String aggregateId;

    @Column(name = "trace_id", length = 100)
    private String traceId;

    @Column(name = "decision", length = 50)
    private String decision;

    @Column(name = "matched_rule", length = 150)
    private String matchedRule;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "input_redacted_json", columnDefinition = "TEXT")
    private String inputRedactedJson;

    @Column(name = "reasons_json", columnDefinition = "TEXT")
    private String reasonsJson;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
}
