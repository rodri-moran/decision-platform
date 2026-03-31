package com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities;

import com.rodrigomoran.decisionplatform.rules_service.domain.enums.ChangeType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;
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
        name="rule_versions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uk_rule_versions_rule_key_version",
                        columnNames={"rule_key","version"}
                )
        },
        indexes = {
                @Index(name="idx_rule_versions_rule_key", columnList="rule_key"),
                @Index(name="idx_rule_versions_status", columnList="status"),
                @Index(name="idx_rule_versions_published_at",columnList = "published_at")
        }
)
public class RuleVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "rule_key")
    private String ruleKey;
    private Integer versionNumber;
    @Column(name = "supersedesVersion")
    private Integer supersedesVersion;
    @Column(name = "definition_json", nullable = false)
    private String definitionJson; // copia inmutable al publicar
    @Column(name = "metadata_json")
    private String metadataJson;
    @Enumerated(EnumType.STRING)
    private RuleVersionStatus status;
    @Column(name = "published_by")
    private String publishedBy;
    @Column(name = "published_at")
    private Instant publishedAt;
    @Column(name = "reactivated_by")
    private String reactivatedBy;
    @Column(name = "reactivated_at")
    private Instant reactivatedAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private ChangeType changeType;
    @Column(name = "trace_id")
    private String traceId;
}