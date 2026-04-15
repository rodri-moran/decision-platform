package com.rodrigomoran.decisionplatform.decision_service.application.service;

import com.rodrigomoran.decisionplatform.decision_service.application.exception.ActiveRuleNotFoundException;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.ActiveRule;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.DecisionResult;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.EvaluationContext;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.ParsedRuleDefinition;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.in.EvaluateDecisionUseCase;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.ActiveRuleRepositoryPort;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.RuleDefinitionParserPort;
import com.rodrigomoran.decisionplatform.decision_service.domain.service.RuleEvaluationEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluateDecisionService implements EvaluateDecisionUseCase {

    private final ActiveRuleRepositoryPort activeRuleRepositoryPort;
    private final RuleDefinitionParserPort ruleDefinitionParserPort;
    private final RuleEvaluationEngine ruleEvaluationEngine;

    @Override
    public DecisionResult execute(String ruleKey, EvaluationContext context) {
        ActiveRule activeRule = activeRuleRepositoryPort.findByRuleKey(ruleKey)
                .orElseThrow(() -> new ActiveRuleNotFoundException(ruleKey));

        ParsedRuleDefinition parsedRuleDefinition =
                ruleDefinitionParserPort.parse(activeRule.getDefinitionJson());

        return ruleEvaluationEngine.evaluate(activeRule, parsedRuleDefinition, context);
    }
}
