package com.rodrigomoran.decisionplatform.audit_service.infrastructure.messaging.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.*;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuditMessageMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", source = "eventType")
    @Mapping(target = "aggregateType", source = "aggregateType")
    @Mapping(target = "aggregateId", source = "aggregateId")
    @Mapping(target = "occurredAt", source = "occurredAt")
    @Mapping(target = "reasonsJson", expression = "java(mapReasons(message.getReasons()))")
    @Mapping(target = "payloadJson", expression = "java(mapPayload(message))")
    @Mapping(target = "createdAt", ignore = true)
    public abstract AuditEventEntity toEntity(DecisionMadeAuditMessage message);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventType", source = "eventType")
    @Mapping(target = "aggregateType", source = "aggregateType")
    @Mapping(target = "aggregateId", source = "aggregateId")
    @Mapping(target = "occurredAt", source = "occurredAt")
    @Mapping(target = "decision", ignore = true)
    @Mapping(target = "matchedRule", ignore = true)
    @Mapping(target = "latencyMs", ignore = true)
    @Mapping(target = "inputRedactedJson", ignore = true)
    @Mapping(target = "reasonsJson", ignore = true)
    @Mapping(target = "payloadJson", expression = "java(mapPayload(message))")
    @Mapping(target = "createdAt", ignore = true)
    public abstract AuditEventEntity toEntity(RulePublishedAuditMessage message);

    public abstract AuditResponse toResponse(AuditEventEntity entity);

    @Mapping(target = "reasons", expression = "java(mapReasons(entity.getReasonsJson()))")
    public abstract AuditDetailResponse toDetailResponse(AuditEventEntity entity);

    public abstract List<AuditResponse> toResponseList(List<AuditEventEntity> entities);

    protected String mapReasons(List<String> reasons) {
        if (reasons == null || reasons.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(reasons);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing audit reasons", ex);
        }
    }

    protected List<String> mapReasons(String reasonsJson) {
        if (reasonsJson == null || reasonsJson.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(reasonsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error deserializing audit reasons", ex);
        }
    }

    protected String mapPayload(Object payload) {
        if (payload == null) {
            return "{}";
        }

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing audit payload", ex);
        }
    }
}