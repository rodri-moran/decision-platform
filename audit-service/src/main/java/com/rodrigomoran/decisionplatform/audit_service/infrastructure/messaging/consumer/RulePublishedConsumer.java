package com.rodrigomoran.decisionplatform.audit_service.infrastructure.messaging.consumer;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.RulePublishedAuditMessage;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.ProcessAuditEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RulePublishedConsumer {

    private final ProcessAuditEventUseCase processAuditEventUseCase;

    @KafkaListener(
            topics = "${app.kafka.topics.rule-published}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "rulePublishedKafkaListenerContainerFactory"
    )
    public void consumeRulePublished(RulePublishedAuditMessage message) {
        try {
            log.info(
                    "Received RULE_PUBLISHED event. eventId={}, ruleKey={}, version={}, traceId={}",
                    message.getEventId(),
                    message.getRuleKey(),
                    message.getVersion(),
                    message.getTraceId()
            );

            processAuditEventUseCase.processRulePublishedEvent(message);

            log.info(
                    "RULE_PUBLISHED event processed successfully. eventId={}, ruleKey={}, version={}, traceId={}",
                    message.getEventId(),
                    message.getRuleKey(),
                    message.getVersion(),
                    message.getTraceId()
            );

        } catch (Exception ex) {
            log.error(
                    "Error processing RULE_PUBLISHED event. eventId={}, ruleKey={}, version={}, traceId={}",
                    message != null ? message.getEventId() : null,
                    message != null ? message.getRuleKey() : null,
                    message != null ? message.getVersion() : null,
                    message != null ? message.getTraceId() : null,
                    ex
            );

            throw ex;
        }
    }
}