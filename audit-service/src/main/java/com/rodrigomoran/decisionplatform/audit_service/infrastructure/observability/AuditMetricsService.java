package com.rodrigomoran.decisionplatform.audit_service.infrastructure.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditMetricsService {

    private final MeterRegistry meterRegistry;

    public void incrementAuditEventProcessed(String eventType) {
        meterRegistry.counter(
                "audit_events_processed_total",
                "event_type", eventType
        ).increment();
    }

    public void incrementAuditEventDuplicated(String eventType) {
        meterRegistry.counter(
                "audit_events_duplicated_total",
                "event_type", eventType
        ).increment();
    }

    public Timer.Sample startProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProcessingTimer(Timer.Sample sample, String eventType) {
        sample.stop(
                Timer.builder("audit_event_processing_duration_seconds")
                        .description("Time taken to process audit events")
                        .tag("event_type", eventType)
                        .register(meterRegistry)
        );
    }
}