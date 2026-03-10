package com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "outbox_events",
        indexes = {
                @Index(name="idx_outbox_event_status",columnList = "status"),
                @Index(name = "idx_outbox_event_aggregate_type", columnList = "aggregate_type"),
                @Index(name = "idx_outbox_event_aggregate_id", columnList = "aggregate_id"),
                @Index(name = "idx_outbox_event_next_attempt_at", columnList = "next_attempt_at")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id")
    private UUID eventId;
    @Column(name = "aggregate_type")
    private String aggregateType;
    @Column(name = "aggregate_id")
    private String aggregateId;
    @Column(name = "event_type")
    private String eventType;
    @Lob
//    @Column(name = "payload_json", columnDefinition = "jsonb")
    @Column(name = "payload_json", nullable = false)

    private String payloadJson;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "ocurred_at")
    private Instant ocurredAt;
    @Nullable
    @Column(name = "sent_at")
    private Instant sentAt;
    @Column(name = "retry_count")
    private int retryCount = 0;
    @Column(name = "last_error")
    private String lastError;
    @Column(name = "next_attempt_at")
    private Instant nextAttemptAt;

    public enum Status{
        PENDING, SENT, FAILED
    }
}