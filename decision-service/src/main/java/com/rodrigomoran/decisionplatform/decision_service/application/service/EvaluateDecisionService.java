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
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.event.DecisionMadeEventPayload;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.publisher.DecisionMadeKafkaPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvaluateDecisionService implements EvaluateDecisionUseCase {

    private final ActiveRuleRepositoryPort activeRuleRepositoryPort;
    private final RuleDefinitionParserPort ruleDefinitionParserPort;
    private final RuleEvaluationEngine ruleEvaluationEngine;
    private final DecisionMadeKafkaPublisher decisionMadeKafkaPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public DecisionResult execute(String ruleKey, EvaluationContext context) {
        long startTime = System.currentTimeMillis();

        ActiveRule activeRule = activeRuleRepositoryPort.findByRuleKey(ruleKey)
                .orElseThrow(() -> new ActiveRuleNotFoundException(ruleKey));

        ParsedRuleDefinition parsedRuleDefinition =
                ruleDefinitionParserPort.parse(activeRule.getDefinitionJson());

        DecisionResult decisionResult =
                ruleEvaluationEngine.evaluate(activeRule, parsedRuleDefinition, context);

        long latencyMs = System.currentTimeMillis() - startTime;

        DecisionMadeEventPayload event = buildDecisionMadeEvent(
                activeRule,
                decisionResult,
                context,
                latencyMs
        );

        decisionMadeKafkaPublisher.publish(event);

        return decisionResult;
    }

    private DecisionMadeEventPayload buildDecisionMadeEvent(
            ActiveRule activeRule,
            DecisionResult decisionResult,
            EvaluationContext context,
            Long latencyMs
    ) {
        String decisionId = UUID.randomUUID().toString();

        return DecisionMadeEventPayload.builder()
                .eventId(UUID.randomUUID())
                .eventType("DECISION_MADE")
                .decisionId(decisionId)
                .decision(decisionResult.isResult() ? "APPROVE" : "REJECT")
                .ruleKey(activeRule.getRuleKey())
                .ruleVersion(activeRule.getVersion())
                .matchedRule(activeRule.getRuleKey())
                .reasons(decisionResult.getReasons())
                .occurredAt(decisionResult.getEvaluatedAt())
                .inputRedactedJson(toRedactedJson(context))
                .latencyMs(latencyMs)
                .traceId(decisionResult.getTraceId())
                .aggregateType("DECISION")
                .aggregateId(decisionId)
                .build();
    }

    private String toRedactedJson(EvaluationContext context) {
        try {
            return objectMapper.writeValueAsString(context.getData());
        } catch (Exception e) {
            return "{}";
        }
    }
}
