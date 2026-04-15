package com.rodrigomoran.decisionplatform.decision_service.application.mapper;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.ActiveRule;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.dto.RulePublishedEventPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface ActiveRuleApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    ActiveRule fromEvent(RulePublishedEventPayload event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    void updateActiveRuleFromEvent(RulePublishedEventPayload event, @MappingTarget ActiveRule activeRule);
}