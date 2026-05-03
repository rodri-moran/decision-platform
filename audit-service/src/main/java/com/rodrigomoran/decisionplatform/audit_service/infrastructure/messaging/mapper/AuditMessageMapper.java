package com.rodrigomoran.decisionplatform.audit_service.infrastructure.messaging.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.*;
import com.rodrigomoran.decisionplatform.audit_service.infrastructure.persistence.entity.AuditEventEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuditMessageMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reasonsJson", expression = "java(reasonsToJson(message.getReasons()))")
    @Mapping(target = "payloadJson", expression = "java(decisionMadePayloadToJson(message))")
    @Mapping(target = "createdAt", ignore = true)
    public abstract AuditEventEntity toEntity(DecisionMadeAuditMessage message);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "decision", ignore = true)
    @Mapping(target = "matchedRule", ignore = true)
    @Mapping(target = "latencyMs", ignore = true)
    @Mapping(target = "inputRedactedJson", ignore = true)
    @Mapping(target = "reasonsJson", ignore = true)
    @Mapping(target = "payloadJson", expression = "java(rulePublishedPayloadToJson(message))")
    @Mapping(target = "createdAt", ignore = true)
    public abstract AuditEventEntity toEntity(RulePublishedAuditMessage message);

    public abstract AuditResponse toResponse(AuditEventEntity entity);

    @Mapping(target = "reasons", expression = "java(jsonToReasons(entity.getReasonsJson()))")
    @Mapping(target = "payload", expression = "java(jsonToObject(entity.getPayloadJson()))")
    @Mapping(target = "inputRedacted", expression = "java(jsonToObject(entity.getInputRedactedJson()))")
    public abstract AuditDetailResponse toDetailResponse(AuditEventEntity entity);

    public abstract List<AuditResponse> toResponseList(List<AuditEventEntity> entities);

    protected String reasonsToJson(List<String> reasons) {
        if (reasons == null || reasons.isEmpty()) {
            return "[]";
        }

        try {
            return objectMapper.writeValueAsString(reasons);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing audit reasons", ex);
        }
    }

    protected List<String> jsonToReasons(String reasonsJson) {
        if (reasonsJson == null || reasonsJson.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(reasonsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error deserializing audit reasons", ex);
        }
    }

    protected String decisionMadePayloadToJson(DecisionMadeAuditMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing decision made audit payload", ex);
        }
    }

    protected String rulePublishedPayloadToJson(RulePublishedAuditMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error serializing rule published audit payload", ex);
        }
    }
    protected Object jsonToObject(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(json, Object.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Error deserializing JSON field", ex);
        }
    }
}