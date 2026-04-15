package com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "consumed_events",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_consumed_events_event_id", columnNames = "event_id")
        }
)
@Builder
public class ConsumedEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    @Column(name = "trace_id", length = 100)
    private String traceId;

}
