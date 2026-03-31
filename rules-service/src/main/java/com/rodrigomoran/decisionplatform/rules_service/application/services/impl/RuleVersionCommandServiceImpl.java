package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.mappers.RuleVersionMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleVersionCommandService;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.ChangeType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.RuleVersionRepository;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RuleVersionCommandServiceImpl implements RuleVersionCommandService {
    private final RuleVersionRepository ruleVersionRepository;
    private final RuleVersionMapper ruleVersionMapper;
    @Override
    @Transactional
    public RuleVersionResponseDTO rollbackToVersion(String ruleKey, Integer targetVersionNumber, String actor, String traceId, String idempotencyKey) {
        validateFields(ruleKey,targetVersionNumber, actor, traceId, idempotencyKey);

        RuleVersion activeVersion = ruleVersionRepository.
                findByRuleKeyAndStatus(ruleKey, RuleVersionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("RuleVersion with ruleKey " + ruleKey + " and ACTIVE not found."));

        RuleVersion targetVersion = ruleVersionRepository
                .findByRuleKeyAndVersionNumber(ruleKey, targetVersionNumber)
                .orElseThrow(() -> new EntityNotFoundException("RuleVersion with ruleKey " + ruleKey + " and version " + targetVersionNumber + " not found."));

        if(activeVersion.getVersionNumber().equals(targetVersionNumber)){
            throw new IllegalArgumentException("Version " + targetVersionNumber + " is already active.");
        }

        activeVersion.setStatus(RuleVersionStatus.INACTIVE);
        targetVersion.setStatus(RuleVersionStatus.ACTIVE);
        targetVersion.setTraceId(traceId);
        targetVersion.setChangeType(ChangeType.ROLLBACK);
        targetVersion.setReactivatedBy(actor);
        targetVersion.setReactivatedAt(Instant.now());

        ruleVersionRepository.save(activeVersion);
        targetVersion =  ruleVersionRepository.save(targetVersion);

        return ruleVersionMapper.toResponseDto(targetVersion);
    }

    private void validateFields(String ruleKey, Integer targetVersionNumber, String actor, String traceId, String idempotencyKey){
        if(ruleKey == null || ruleKey.isBlank()){
            throw new IllegalArgumentException("RuleKey must not be null");
        }
        if(targetVersionNumber == null){
            throw new IllegalArgumentException("TargetVersionNumber must not be null");
        }
        if(actor == null || actor.isBlank()){
            throw new IllegalArgumentException("Actor must not be null");
        }
        if(traceId == null || traceId.isBlank()){
            throw new IllegalArgumentException("TraceId must not be null");
        }
        if(idempotencyKey == null || idempotencyKey.isBlank()){
            throw new IllegalArgumentException("IdempotencyKey must not be null");
        }
    }
}