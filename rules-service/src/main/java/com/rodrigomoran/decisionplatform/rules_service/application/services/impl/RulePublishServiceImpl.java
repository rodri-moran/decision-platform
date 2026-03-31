package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.mappers.RuleDraftMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.mappers.RuleVersionMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.OutboxCommandService;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RulePublishService;
import com.rodrigomoran.decisionplatform.rules_service.application.validators.RuleDefinitionValidator;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.ChangeType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.IdempotencyOperationType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleDraftNotPublishableException;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleDraftNotFoundException;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleDraft;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.RuleDraftRepository;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.RuleVersionRepository;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RulePublishServiceImpl implements RulePublishService {
    private final IdempotencyService idempotencyService;
    private final RuleDraftRepository ruleDraftRepository;
    private final RuleVersionRepository ruleVersionRepository;
    private final RuleDefinitionValidator validator;
    private final RuleDraftMapper ruleDraftMapper;
    private final RuleVersionMapper ruleVersionMapper;
    private final OutboxCommandService outboxCommandService;

    @Override
    @Transactional
    public RuleVersionResponseDTO publishDraft(Long draftId, String actor, String traceId, String idempotencyKey) {
        validateFields(idempotencyKey, draftId, actor, traceId);

        String requestHash = idempotencyService.buildHash(
                Map.of(
                        "draftId", draftId
                )
        );

        Optional<RuleVersionResponseDTO> existingResponse =
                idempotencyService.findCompletedResponse(
                        IdempotencyOperationType.PUBLISH_RULE_DRAFT,
                        idempotencyKey,
                        requestHash,
                        RuleVersionResponseDTO.class
                );

        if (existingResponse.isPresent()) {
            return existingResponse.get();
        }

        idempotencyService.startProcessing(
                IdempotencyOperationType.PUBLISH_RULE_DRAFT,
                idempotencyKey,
                requestHash
        );

        try {
            RuleDraft draft = ruleDraftRepository.findById(draftId)
                    .orElseThrow(() -> new RuleDraftNotFoundException(draftId));

            if (draft.getStatus() != RuleDraftStatus.DRAFT) {
                throw new RuleDraftNotPublishableException();
            }

            Integer nextVersionNumber = 1;

            RuleVersion lastVersion = ruleVersionRepository
                    .findTopByRuleKeyOrderByVersionNumberDesc(draft.getRuleKey())
                    .orElse(null);

            if (lastVersion != null) {
                nextVersionNumber = Math.toIntExact(lastVersion.getVersionNumber() + 1);
            }

            RuleVersion activeVersion = ruleVersionRepository
                    .findByRuleKeyAndStatus(draft.getRuleKey(), RuleVersionStatus.ACTIVE)
                    .orElse(null);

            if (activeVersion != null) {
                activeVersion.setStatus(RuleVersionStatus.INACTIVE);
                ruleVersionRepository.save(activeVersion);
            }

            RuleVersion ruleVersion = new RuleVersion();
            ruleVersion.setRuleKey(draft.getRuleKey());
            ruleVersion.setVersionNumber(nextVersionNumber);
            ruleVersion.setDefinitionJson(draft.getDefinitionJson());
            ruleVersion.setMetadataJson(draft.getMetadataJson());
            ruleVersion.setDescription(draft.getDescription());
            ruleVersion.setName(draft.getName());
            ruleVersion.setStatus(RuleVersionStatus.ACTIVE);
            ruleVersion.setPublishedBy(actor);
            Instant publishedAt = Instant.now();
            ruleVersion.setPublishedAt(publishedAt);
            ruleVersion.setChangeType(ChangeType.PUBLISH);
            ruleVersion.setTraceId(traceId);

            ruleVersion = ruleVersionRepository.save(ruleVersion);

            draft.setStatus(RuleDraftStatus.PUBLISHED);
            draft.setUpdatedBy(actor);
            draft.setTraceId(traceId);
            ruleDraftRepository.save(draft);

            // TODO: crear y persistir OutboxEvent
            outboxCommandService.enqueueRulePublishedEvent(ruleVersion.getRuleKey(), ruleVersion.getVersionNumber(), actor, publishedAt, traceId);

            RuleVersionResponseDTO response = ruleVersionMapper.toResponseDto(ruleVersion);

            idempotencyService.markCompleted(
                    IdempotencyOperationType.PUBLISH_RULE_DRAFT,
                    idempotencyKey,
                    response
            );

            return response;

        } catch (RuntimeException ex) {
            idempotencyService.markFailed(
                    IdempotencyOperationType.PUBLISH_RULE_DRAFT,
                    idempotencyKey,
                    ex.getMessage()
            );
            throw ex;
        }
    }

    private void validateFields(String idempotencyKey, Long draftId, String actor, String traceId) {
        if (draftId == null) {
            throw new IllegalArgumentException("Draft id must not be null");
        }

        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("Actor must not be null or blank");
        }

        if (traceId == null || traceId.isBlank()) {
            throw new IllegalArgumentException("TraceId must not be null or blank");
        }

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency key must not be null or blank");
        }
    }
}