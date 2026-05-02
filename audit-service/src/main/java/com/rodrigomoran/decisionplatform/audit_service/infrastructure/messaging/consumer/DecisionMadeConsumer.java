package com.rodrigomoran.decisionplatform.audit_service.infrastructure.messaging.consumer;

import com.rodrigomoran.decisionplatform.audit_service.application.dto.DecisionMadeAuditMessage;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.ProcessAuditEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DecisionMadeConsumer {

    private final ProcessAuditEventUseCase processAuditEventUseCase;

    @KafkaListener(
            topics = "${app.kafka.topics.decision-made}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "decisionMadeKafkaListenerContainerFactory"
    )
    public void consumeDecisionMade(DecisionMadeAuditMessage message) {

        try {
            log.info(
                    "Received DECISION_MADE event. eventId={}, decisionId={}, occurredAt={}, traceId={}",
                    message.getEventId(),
                    message.getDecisionId(),
                    message.getOccurredAt(),
                    message.getTraceId()
            );

            log.info(
                    "Received DECISION_MADE event. eventId={}, decisionId={}, traceId={}",
                    message.getEventId(),
                    message.getDecisionId(),
                    message.getTraceId()
            );

            processAuditEventUseCase.processDecisionMadeEvent(message);

            log.info(
                    "DECISION_MADE event processed successfully. eventId={}, decisionId={}, traceId={}",
                    message.getEventId(),
                    message.getDecisionId(),
                    message.getTraceId()
            );

        } catch (Exception ex) {
            log.error(
                    "Error processing DECISION_MADE event. eventId={}, decisionId={}, traceId={}",
                    message != null ? message.getEventId() : null,
                    message != null ? message.getDecisionId() : null,
                    message != null ? message.getTraceId() : null,
                    ex
            );

            throw ex;
        }
    }
}