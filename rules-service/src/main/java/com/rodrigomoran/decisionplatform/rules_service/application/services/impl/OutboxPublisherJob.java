package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.OutboxPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisherJob {
    private final OutboxPublisherService outboxPublisherService;


    @Scheduled(fixedDelay = 5000)
    public void publishPendingEventsJob(){
        //corre cada x tiempo
        //llama al service publisher
        //no tiene que tener lógica pesada adentro
        //captura errores globales para que scheduled no se rompa
        log.info("Outbox publisher job started");
        try {
            outboxPublisherService.publishPendingEvents();
            log.info("Outbox publisher job finished");
        } catch (Exception ex) {
            log.error("Outbox publisher job failed", ex);
        }
    }
}