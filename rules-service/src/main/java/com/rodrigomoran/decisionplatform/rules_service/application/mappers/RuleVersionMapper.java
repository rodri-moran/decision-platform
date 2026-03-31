package com.rodrigomoran.decisionplatform.rules_service.application.mappers;

import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring")

public abstract class RuleVersionMapper {
    @Autowired
    protected ObjectMapper objectMapper;

    public abstract RuleVersionResponseDTO toResponseDto(RuleVersion ruleVersion);
}
