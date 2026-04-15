package com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "active_rules",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_active_rules_rule_key", columnNames = "rule_key")
        }
)
public class ActiveRuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_key", nullable = false, length = 100)
    private String ruleKey;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "definition_json", nullable = false, columnDefinition = "TEXT")
    private String definitionJson;

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

    @Column(name = "published_at", nullable = false)
    private Instant publishedAt;

    @Column(name = "published_by", length = 100)
    private String publishedBy;

    @Column(name = "trace_id", length = 100)
    private String traceId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
