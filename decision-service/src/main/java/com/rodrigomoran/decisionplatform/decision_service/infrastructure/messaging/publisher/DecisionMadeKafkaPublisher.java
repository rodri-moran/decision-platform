package com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.publisher;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.event.DecisionMadeEventPayload;
import com.rodrigomoran.decisionplatform.decision_service.shared.constants.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DecisionMadeKafkaPublisher {

    private final KafkaTemplate<String, DecisionMadeEventPayload> kafkaTemplate;

    public void publish(DecisionMadeEventPayload event) {
        log.info(
                "Publishing DECISION_MADE event. eventId={}, decisionId={}, ruleKey={}, traceId={}",
                event.getEventId(),
                event.getDecisionId(),
                event.getRuleKey(),
                event.getTraceId()
        );

        kafkaTemplate.send(
                KafkaTopics.DECISION_MADE,
                event.getDecisionId(),
                event
        );
    }
}