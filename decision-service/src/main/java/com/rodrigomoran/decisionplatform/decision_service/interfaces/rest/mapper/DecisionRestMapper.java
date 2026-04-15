package com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.mapper;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.DecisionResult;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.EvaluationContext;
import com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.dto.DecisionResponseDTO;
import com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.dto.EvaluateDecisionRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DecisionRestMapper {

    EvaluationContext toContext(EvaluateDecisionRequestDTO requestDTO);

    DecisionResponseDTO toResponse(DecisionResult decisionResult);
}
