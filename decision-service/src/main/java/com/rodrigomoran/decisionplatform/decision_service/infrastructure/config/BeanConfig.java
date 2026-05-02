package com.rodrigomoran.decisionplatform.decision_service.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigomoran.decisionplatform.decision_service.application.mapper.ActiveRuleApplicationMapper;
import com.rodrigomoran.decisionplatform.decision_service.application.service.EvaluateDecisionService;
import com.rodrigomoran.decisionplatform.decision_service.application.service.HandleRulePublishedEventService;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.in.EvaluateDecisionUseCase;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.in.HandleRulePublishedEventUseCase;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.ActiveRuleRepositoryPort;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.RuleDefinitionParserPort;
import com.rodrigomoran.decisionplatform.decision_service.domain.service.RuleEvaluationEngine;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.publisher.DecisionMadeKafkaPublisher;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.parser.JacksonRuleDefinitionParserAdapter;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.adapter.ActiveRuleRepositoryAdapter;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.repository.SpringDataActiveRuleRepository;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.repository.SpringDataConsumedEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RuleEvaluationEngine ruleEvaluationEngine() {
        return new RuleEvaluationEngine();
    }

    @Bean
    public ActiveRuleRepositoryPort activeRuleRepositoryPort(SpringDataActiveRuleRepository repository) {
        return new ActiveRuleRepositoryAdapter(repository);
    }

    @Bean
    public RuleDefinitionParserPort ruleDefinitionParserPort(ObjectMapper objectMapper) {
        return new JacksonRuleDefinitionParserAdapter(objectMapper);
    }

    @Bean
    public EvaluateDecisionUseCase evaluateDecisionUseCase(
            ActiveRuleRepositoryPort activeRuleRepositoryPort,
            RuleDefinitionParserPort ruleDefinitionParserPort,
            RuleEvaluationEngine ruleEvaluationEngine,
            DecisionMadeKafkaPublisher decisionMadeKafkaPublisher,
            ObjectMapper objectMapper
    ) {
        return new EvaluateDecisionService(
                activeRuleRepositoryPort,
                ruleDefinitionParserPort,
                ruleEvaluationEngine,
                decisionMadeKafkaPublisher,
                objectMapper
        );
    }

    @Bean
    public HandleRulePublishedEventUseCase handleRulePublishedEventUseCase(
            ActiveRuleRepositoryPort activeRuleRepositoryPort,
            SpringDataConsumedEventRepository consumedEventRepository,
            ActiveRuleApplicationMapper activeRuleApplicationMapper
    ) {
        return new HandleRulePublishedEventService(
                activeRuleRepositoryPort,
                consumedEventRepository,
                activeRuleApplicationMapper
        );
    }
}
