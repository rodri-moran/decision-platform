package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.IdempotencyOperationType;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.IdempotencyStatus;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.IdempotencyConflictException;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.IdempotencyKeyReusedWithDifferentPayloadException;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.Idempotency;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final ObjectMapper objectMapper;


    @Transactional
    public <T> Optional<T> findCompletedResponse (
            IdempotencyOperationType operationType,
            String idempotencyKey,
            String requestHash,
            Class<T> responseClass
    ) {
        Optional<Idempotency> optionalRecord =
                repository.findByOperationTypeAndIdempotencyKey(operationType, idempotencyKey);
        if (optionalRecord.isEmpty()) {
            return Optional.empty();
        }

        Idempotency record = optionalRecord.get();

        validateRequestHash(record, requestHash);

        if (record.getStatus() == IdempotencyStatus.COMPLETED) {
            return Optional.of(deserialize(record.getResponsePayload(), responseClass));
        }

        if (record.getStatus() == IdempotencyStatus.PROCESSING) {
            throw new IdempotencyConflictException(
                    "Ya existe una operación en curso para la misma idempotencyKey"
            );
        }

        throw new IdempotencyConflictException(
                "La operación anterior con la misma idempotencyKey falló. Usá una nueva key para reintentar"
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void startProcessing(
            IdempotencyOperationType operationType,
            String idempotencyKey,
            String requestHash
    ) {
        Idempotency record = new Idempotency();
        record.setOperationType(operationType);
        record.setIdempotencyKey(idempotencyKey);
        record.setRequestHash(requestHash);
        record.markProcessing();

        try {
            repository.saveAndFlush(record);
        } catch (DataIntegrityViolationException ex) {
            throw new IdempotencyConflictException(
                    "La operación ya fue iniciada o procesada con la misma idempotencyKey"
            );
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompleted(
            IdempotencyOperationType operationType,
            String idempotencyKey,
            Object response
    ) {
        Idempotency record = repository.findByOperationTypeAndIdempotencyKey(operationType, idempotencyKey)
                .orElseThrow(() -> new IdempotencyConflictException("No se encontró registro de idempotencia"));

        String payload = serialize(response);
        record.markCompleted(payload, response.getClass().getName());
        repository.save(record);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(
            IdempotencyOperationType operationType,
            String idempotencyKey,
            String errorMessage
    ) {
        repository.findByOperationTypeAndIdempotencyKey(operationType, idempotencyKey)
                .ifPresent(record -> {
                    record.markFailed(errorMessage);
                    repository.save(record);
                });
    }

    public String buildHash(Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo generar requestHash", e);
        }
    }

    private void validateRequestHash(Idempotency record, String requestHash) {
        if (record.getRequestHash() == null || requestHash == null) {
            return;
        }

        if (!record.getRequestHash().equals(requestHash)) {
            throw new IdempotencyKeyReusedWithDifferentPayloadException(
                    "La misma idempotencyKey fue reutilizada con un payload distinto"
            );
        }
    }

    private String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo serializar la respuesta idempotente", e);
        }
    }

    private <T> T deserialize(String payload, Class<T> responseClass) {
        try {
            return objectMapper.readValue(payload, responseClass);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo deserializar la respuesta idempotente", e);
        }
    }
}
