package com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.OutboxEvent;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public interface OutboxPublisherService {

    void publishPendingEvents();

    void publishSingleEvent(OutboxEvent event);

    void handlePublishSuccess(Long outboxEventId, Instant sentAt);

    void handlePublishFailure(Long outboxEventId, String errorMessage);
}