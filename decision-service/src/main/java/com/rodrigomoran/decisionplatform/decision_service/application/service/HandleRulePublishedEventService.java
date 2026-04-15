package com.rodrigomoran.decisionplatform.decision_service.application.service;

import com.rodrigomoran.decisionplatform.decision_service.application.mapper.ActiveRuleApplicationMapper;
import com.rodrigomoran.decisionplatform.decision_service.domain.model.ActiveRule;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.in.HandleRulePublishedEventUseCase;
import com.rodrigomoran.decisionplatform.decision_service.domain.port.out.ActiveRuleRepositoryPort;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.messaging.dto.RulePublishedEventPayload;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.entity.ConsumedEventEntity;
import com.rodrigomoran.decisionplatform.decision_service.infrastructure.persistence.repository.SpringDataConsumedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class HandleRulePublishedEventService implements HandleRulePublishedEventUseCase {

    private final ActiveRuleRepositoryPort activeRuleRepositoryPort;
    private final SpringDataConsumedEventRepository consumedEventRepository;
    private final ActiveRuleApplicationMapper activeRuleApplicationMapper;


    @Override
    public void handle(RulePublishedEventPayload event) {
        try {
            log.info("Starting processing for eventId={}", event.getEventId());

            if (consumedEventRepository.existsByEventId(event.getEventId())) {
                log.warn("Event already processed: {}", event.getEventId());
                return;
            }

            ActiveRule activeRule = activeRuleRepositoryPort.findByRuleKey(event.getRuleKey())
                    .map(existing -> {
                        activeRuleApplicationMapper.updateActiveRuleFromEvent(event, existing);
                        return existing;
                    })
                    .orElseGet(() -> activeRuleApplicationMapper.fromEvent(event));

            activeRuleRepositoryPort.save(activeRule);
            log.info("ActiveRule saved");

            consumedEventRepository.save(
                    ConsumedEventEntity.builder()
                            .eventId(event.getEventId())
                            .eventType(event.getEventType())
                            .aggregateId(event.getRuleKey())
                            .processedAt(Instant.now())
                            .traceId(event.getTraceId())
                            .build()
            );

            log.info("ConsumedEvent saved");
        } catch (Exception e) {
            log.error("Error processing Kafka event", e);
            throw e;
        }
    }
}
