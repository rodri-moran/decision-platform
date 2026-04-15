package com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "rule_drafts",
        indexes = {
                @Index(name="idx_rule_drafts_rule_key", columnList="rule_key"),
                @Index(name="idx_rule_drafts_status", columnList="rule_draft_status"),
                @Index(name="idx_rule_drafts_updated_at", columnList="updated_at")
        }
)
public class RuleDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "rule_key")
    private String ruleKey;
    @Column(nullable = false)
    private String name; // Nombre humano. Ej.: "Antifraude - Pago con tarjeta"
    private String description;// Desc humana, ej: Rechazar pagos si el dispositivo es riesgoso y el usuario es nuevo.
//    @Column(nullable = false, name = "definition_json", columnDefinition = "jsonb")
    @Column(nullable = false, name = "definition_json")
    private String definitionJson;
//    @Column(name = "metadata_json", columnDefinition = "jsonb")
    @Column(name = "metadata_json")
    private String metadataJson; // tags, owners, notes,etc.
    @Column(nullable = false, name = "rule_draft_status")
    @Enumerated(EnumType.STRING)
    private RuleDraftStatus status;
    @NotNull
    @Column(name = "created_by")
    private String createdBy; // userName desde el token
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(nullable = false, name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "trace_id")
    private String traceId;
    @Version // Con esto, si dos requests pisan el mismo draft, JPA tira OptimisticLockException
    private Long version;
    @PrePersist
    public void onPrePersit(){
        this.updatedAt = Instant.now();
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updatedAt = Instant.now();
    }
}