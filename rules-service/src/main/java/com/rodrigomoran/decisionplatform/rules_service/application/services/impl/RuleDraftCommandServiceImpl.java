package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.mappers.RuleDraftMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleDraftCommandService;
import com.rodrigomoran.decisionplatform.rules_service.application.validators.RuleDefinitionValidator;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleDraftNotPublishableException;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleDraftNotFoundException;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleKeyAlreadyExistsException;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleDraft;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.RuleDraftRepository;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.CreateRuleDraftRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.UpdateRuleDraftRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
@RequiredArgsConstructor
public class RuleDraftCommandServiceImpl implements RuleDraftCommandService {
    private final RuleDraftRepository ruleDraftRepository;
    private final RuleDefinitionValidator validator;
    private final RuleDraftMapper ruleDraftMapper;
    @Override
    public RuleDraftResponseDTO createDraft(CreateRuleDraftRequestDTO request, String actor, String traceId) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Request must not be null");
        }

        if (Objects.isNull(request.ruleKey()) || request.ruleKey().isBlank()) {
            throw new IllegalArgumentException("Rule key must not be null or empty");
        }

        if (Objects.isNull(actor) || actor.isBlank()) {
            throw new IllegalArgumentException("Actor must not be null or blank");
        }

        if (Objects.isNull(traceId) || traceId.isBlank()) {
            throw new IllegalArgumentException("TraceId must not be null or blank");
        }

        if (ruleDraftRepository.existsByRuleKey(request.ruleKey())) {
            throw new RuleKeyAlreadyExistsException(request.ruleKey());
        }

        validator.validateOrThrow(request.definitionJson());

        RuleDraft ruleDraft = ruleDraftMapper.toEntity(request);

        ruleDraft.setStatus(RuleDraftStatus.DRAFT);
        ruleDraft.setCreatedBy(actor);
        ruleDraft.setUpdatedBy(actor);
        ruleDraft.setTraceId(traceId);

        ruleDraft = ruleDraftRepository.save(ruleDraft);

        return ruleDraftMapper.toResponseDTO(ruleDraft);
    }

    @Override
    public RuleDraftResponseDTO updateDraft(Long draftId, UpdateRuleDraftRequestDTO request, String actor, String traceId) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Request must not be null");
        }

        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("Actor must not be null or blank");
        }

        if (traceId == null || traceId.isBlank()) {
            throw new IllegalArgumentException("TraceId must not be null or blank");
        }

        if (request.name() == null
                && request.description() == null
                && request.definitionJson() == null
                && request.metadataJson() == null) {
            throw new IllegalArgumentException("At least one field must be provided");
        }

        RuleDraft ruleDraft = ruleDraftRepository.findById(draftId)
                .orElseThrow(() -> new RuleDraftNotFoundException(draftId));

        if (!ruleDraft.getStatus().equals(RuleDraftStatus.DRAFT)) {
            throw new RuleDraftNotPublishableException();
        }

        if (request.name() != null && request.name().isBlank()) {
            throw new IllegalArgumentException("The name must not be empty");
        }

        if (request.description() != null && request.description().isBlank()) {
            throw new IllegalArgumentException("The description must not be empty");
        }

        if (request.definitionJson() != null) {
            validator.validateOrThrow(request.definitionJson());
        }

        ruleDraftMapper.updateDraftFromDto(request, ruleDraft);

        ruleDraft.setUpdatedBy(actor);
        ruleDraft.setTraceId(traceId);

        ruleDraft = ruleDraftRepository.save(ruleDraft);

        return ruleDraftMapper.toResponseDTO(ruleDraft);
    }

    @Override
    public void archiveDraft(Long draftId, String actor, String traceId) {
        if (actor == null || actor.isBlank()) {
            throw new IllegalArgumentException("Actor must not be null or blank");
        }

        if (traceId == null || traceId.isBlank()) {
            throw new IllegalArgumentException("TraceId must not be null or blank");
        }

        RuleDraft ruleDraft = ruleDraftRepository.findById(draftId)
                .orElseThrow(() -> new RuleDraftNotFoundException(draftId));

        if(ruleDraft.getStatus() == RuleDraftStatus.ARCHIVED){
            return;
        }

        ruleDraft.setStatus(RuleDraftStatus.ARCHIVED);
        ruleDraft.setUpdatedBy(actor);
        ruleDraft.setTraceId(traceId);
        ruleDraftRepository.save(ruleDraft);
    }
}