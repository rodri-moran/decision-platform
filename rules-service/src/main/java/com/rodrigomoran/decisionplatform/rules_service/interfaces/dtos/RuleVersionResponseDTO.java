package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.ChangeType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;
import java.time.Instant;
public record RuleVersionResponseDTO(Long id,
                                     String ruleKey,
                                     Integer versionNumber,
                                     RuleVersionStatus status,
                                     String publishedBy,
                                     Instant publishedAt,
                                     String metadataJson,
                                     String reactivatedBy,
                                     Instant reactivatedAt,
                                     ChangeType changeType,
                                     String definitionJson,
                                     String traceId
) {
}
// TODO: Validar idempotencyKey si el caso de uso lo requiere
// TODO: Verificar si ya existe un resultado previo asociado a esa idempotencyKey
// TODO: Buscar draft por id
// TODO: Si no existe, lanzar RuleDraftNotFoundException
// TODO: Verificar que el draft esté en estado DRAFT
// TODO: Validar nuevamente definitionJson y metadataJson
// TODO: Buscar la última versión existente para el mismo ruleKey
// TODO: Calcular nextVersionNumber = última versión + 1
// TODO: Buscar versión ACTIVE actual para el mismo ruleKey
// TODO: Si existe, marcarla como INACTIVE
// TODO: Crear nueva RuleVersion con los datos del draft
// TODO: Setear status = ACTIVE
// TODO: Setear publishedBy, publishedAt, changeType = PUBLISH y traceId
// TODO: Persistir nueva versión
// TODO: Cambiar el estado del draft a PUBLISHED si esa es tu política
// TODO: Crear evento de dominio RulePublishedEvent
// TODO: Crear OutboxEvent en estado PENDING
// TODO: Persistir outbox en la misma transacción
// TODO: Guardar resultado asociado a idempotencyKey si usás idempotencia
// TODO: Mapear RuleVersion a RuleVersionResponseDTO
// TODO: Retornar respuesta