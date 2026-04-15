package com.rodrigomoran.decisionplatform.decision_service.interfaces.rest;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.DecisionResult;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.EvaluationContext;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.in.EvaluateDecisionUseCase;
import com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.dto.DecisionResponseDTO;
import com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.dto.EvaluateDecisionRequestDTO;
import com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.mapper.DecisionRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/decisions")
@RequiredArgsConstructor
public class DecisionController {

    private final EvaluateDecisionUseCase evaluateDecisionUseCase;
    private final DecisionRestMapper decisionRestMapper;

    @PostMapping("/evaluate")
    public ResponseEntity<DecisionResponseDTO> evaluate(@RequestBody EvaluateDecisionRequestDTO requestDTO) {
        EvaluationContext context = decisionRestMapper.toContext(requestDTO);
        DecisionResult decisionResult = evaluateDecisionUseCase.execute(requestDTO.getRuleKey(), context);
        return ResponseEntity.ok(decisionRestMapper.toResponse(decisionResult));
    }
}