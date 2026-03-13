package com.rodrigomoran.decisionplatform.rules_service.application.mappers;
import com.rodrigomoran.decisionplatform.rules_service.application.simulation.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleDraft;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.CreateRuleDraftRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.UpdateRuleDraftRequestDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;
@Mapper(componentModel = "spring")
public abstract class RuleDraftMapper {

    @Autowired
    protected ObjectMapper objectMapper;
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "traceId", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "definitionJson", source = "definitionJson")
    @Mapping(target = "metadataJson", source = "metadataJson")
    public abstract RuleDraft toEntity(CreateRuleDraftRequestDTO request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "definitionJson", source = "definitionJson")
    @Mapping(target = "metadataJson", source = "metadataJson")
    public abstract void updateDraftFromDto(UpdateRuleDraftRequestDTO request, @MappingTarget RuleDraft ruleDraft);

    @Mapping(target = "definitionJson", expression = "java(toParsedRuleDefinition(ruleDraft.getDefinitionJson()))")
    @Mapping(target = "metadataJson", expression = "java(toMetadataMap(ruleDraft.getMetadataJson()))")
    public abstract RuleDraftResponseDTO toResponseDTO(RuleDraft ruleDraft);

    protected String map(ParsedRuleDefinition definition) {
        if (definition == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(definition);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error serializing definitionJson", e);
        }
    }

    protected String map(Map<String, Object> metadata) {
        if (metadata == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error serializing metadataJson", e);
        }
    }

    protected ParsedRuleDefinition toParsedRuleDefinition(String definitionJson) {
        if (definitionJson == null || definitionJson.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(definitionJson, ParsedRuleDefinition.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing definitionJson", e);
        }
    }

    protected Map<String, Object> toMetadataMap(String metadataJson) {
        if (metadataJson == null || metadataJson.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(
                    metadataJson,
                    new tools.jackson.core.type.TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing metadataJson", e);
        }
    }


}