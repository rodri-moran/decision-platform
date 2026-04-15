package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.OutboxPublisherService;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.OutboxEvent;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.OutboxEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherServiceImpl implements OutboxPublisherService {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishPendingEvents() {
        List<OutboxEvent> events =
                outboxEventRepository.findTop50ByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
                        OutboxEvent.Status.PENDING,
                        Instant.now()
                );

        log.info("Found {} pending outbox events ready to publish", events.size());

        for (OutboxEvent event : events) {
            publishSingleEvent(event);
        }
    }

    @Override
    public void publishSingleEvent(OutboxEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Outbox event must not be null");
        }

        String topic = resolveTopicName(event.getEventType());

        try {
            log.info("Publishing event {} to topic {}", event.getEventId(), topic);

            kafkaTemplate.send(
                    topic,
                    event.getAggregateId(),
                    event.getPayloadJson()
            ).get();

            log.info("Event {} successfully published", event.getEventId());

            handlePublishSuccess(event.getId(), Instant.now());

        } catch (Exception ex) {
            log.error("Failed to publish event {}", event.getEventId(), ex);

            handlePublishFailure(event.getId(), ex.getMessage());
        }
    }

    @Override
    public void handlePublishSuccess(Long outboxEventId, Instant sentAt) {
        OutboxEvent event = outboxEventRepository.findById(outboxEventId)
                .orElseThrow(() -> new EntityNotFoundException("Outbox with id " + outboxEventId + " not found."));
        event.setStatus(OutboxEvent.Status.SENT);
        event.setSentAt(sentAt);
        event.setLastError(null);

        outboxEventRepository.save(event);
    }

    @Override
    public void handlePublishFailure(Long outboxEventId, String errorMessage) {
        OutboxEvent event = outboxEventRepository.findById(outboxEventId)
                .orElseThrow(() -> new EntityNotFoundException("Outbox with id " + outboxEventId + " not found."));
        event.setLastError(errorMessage);
        int nextRetryCount = event.getRetryCount() + 1;

        event.setRetryCount(nextRetryCount);
        event.setNextAttemptAt(Instant.now().plusSeconds(30));

        if (nextRetryCount >= 5) {
            event.setStatus(OutboxEvent.Status.FAILED);
        } else {
            event.setStatus(OutboxEvent.Status.PENDING);
        }

        outboxEventRepository.save(event);
    }

    private Instant calculateNextAttemptAt(int retryCount) {
        return switch (retryCount) {
            case 1 -> Instant.now().plusSeconds(30);
            case 2 -> Instant.now().plusSeconds(60);
            case 3 -> Instant.now().plusSeconds(120);
            default -> Instant.now().plusSeconds(300);
        };
    }
    private String resolveTopicName(String eventType) {
        return switch (eventType) {
            case "RULE_PUBLISHED" -> "rules.rule-published";
            default -> throw new IllegalArgumentException("Unsupported event type: " + eventType);
        };
    }
}
