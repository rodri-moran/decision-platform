package com.rodrigomoran.decisionplatform.decision_service.domain.port.in;

import com.rodrigomoran.decisionplatform.decision_service.domain.model.DecisionResult;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.EvaluationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public interface EvaluateDecisionUseCase {
    DecisionResult execute(String ruleKey, EvaluationContext context);
}