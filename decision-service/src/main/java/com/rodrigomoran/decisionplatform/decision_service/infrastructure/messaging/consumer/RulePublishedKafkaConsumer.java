package com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.consumer;

import com.rodrigomoran.decisionplatform.decision_service.domain.port.in.HandleRulePublishedEventUseCase;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.dto.RulePublishedEventPayload;
import com.rodrigomoran.decisionplatform.decision_service.shared.constants.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RulePublishedKafkaConsumer {

    private final HandleRulePublishedEventUseCase handleRulePublishedEventUseCase;

    @KafkaListener(
            topics = KafkaTopics.RULE_PUBLISHED,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(RulePublishedEventPayload payload) {
        log.info("Kafka message received in decision-service. eventId={}, ruleKey={}, version={}",
                payload.getEventId(), payload.getRuleKey(), payload.getVersion());
        handleRulePublishedEventUseCase.handle(payload);
        log.info("Kafka message processed successfully. eventId={}", payload.getEventId());
    }
}