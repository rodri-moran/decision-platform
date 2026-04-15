package com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.adapter;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.ActiveRule;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.ActiveRuleRepositoryPort;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.entity.ActiveRuleEntity;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.repository.SpringDataActiveRuleRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ActiveRuleRepositoryAdapter implements ActiveRuleRepositoryPort {

    private final SpringDataActiveRuleRepository repository;

    @Override
    public Optional<ActiveRule> findByRuleKey(String ruleKey) {
        return repository.findByRuleKey(ruleKey).map(this::toDomain);
    }

    @Override
    public ActiveRule save(ActiveRule activeRule) {
        ActiveRuleEntity entity = toEntity(activeRule);
        ActiveRuleEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    private ActiveRule toDomain(ActiveRuleEntity entity) {
        ActiveRule activeRule = new ActiveRule();
        activeRule.setId(entity.getId());
        activeRule.setRuleKey(entity.getRuleKey());
        activeRule.setVersion(entity.getVersion());
        activeRule.setName(entity.getName());
        activeRule.setDescription(entity.getDescription());
        activeRule.setDefinitionJson(entity.getDefinitionJson());
        activeRule.setMetadataJson(entity.getMetadataJson());
        activeRule.setPublishedAt(entity.getPublishedAt());
        activeRule.setPublishedBy(entity.getPublishedBy());
        activeRule.setTraceId(entity.getTraceId());
        activeRule.setCreatedAt(entity.getCreatedAt());
        activeRule.setUpdatedAt(entity.getUpdatedAt());
        return activeRule;
    }

    private ActiveRuleEntity toEntity(ActiveRule activeRule) {
        ActiveRuleEntity entity = new ActiveRuleEntity();
        entity.setId(activeRule.getId());
        entity.setRuleKey(activeRule.getRuleKey());
        entity.setVersion(activeRule.getVersion());
        entity.setName(activeRule.getName());
        entity.setDescription(activeRule.getDescription());
        entity.setDefinitionJson(activeRule.getDefinitionJson());
        entity.setMetadataJson(activeRule.getMetadataJson());
        entity.setPublishedAt(activeRule.getPublishedAt());
        entity.setPublishedBy(activeRule.getPublishedBy());
        entity.setTraceId(activeRule.getTraceId());
        entity.setCreatedAt(activeRule.getCreatedAt());
        entity.setUpdatedAt(activeRule.getUpdatedAt());
        return entity;
    }
}