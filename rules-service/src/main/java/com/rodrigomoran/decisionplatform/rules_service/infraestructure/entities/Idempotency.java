package com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.IdempotencyOperationType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "idempotency",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_idempotency_operation_key",
                        columnNames = {"operation_type", "idempotency_key"}
                )
        }
)
@Data
public class Idempotency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private IdempotencyOperationType operationType;

    @Column(name = "idempotency_key", nullable = false, length = 255)
    private String idempotencyKey;

    @Column(name = "request_hash", length = 128)
    private String requestHash;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status;

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "response_class", length = 255)
    private String responseClass;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    private Long version;

    public void markProcessing() {
        this.status = IdempotencyStatus.PROCESSING;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCompleted(String responsePayload, String responseClass) {
        this.status = IdempotencyStatus.COMPLETED;
        this.responsePayload = responsePayload;
        this.responseClass = responseClass;
        this.errorMessage = null;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markFailed(String errorMessage) {
        this.status = IdempotencyStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = OffsetDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}