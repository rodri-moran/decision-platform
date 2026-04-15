package com.rodrigomoran.decisionplatform.decision_service.interfaces.advice;

import com.rodrigomoran.decisionplatform.decision_service.application.exception.ActiveRuleNotFoundException;
import com.rodrigomoran.decisionplatform.decision_service.application.exception.InvalidRuleDefinitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ActiveRuleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleActiveRuleNotFound(ActiveRuleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", Instant.now().toString(),
                "error", "ACTIVE_RULE_NOT_FOUND",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(InvalidRuleDefinitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRuleDefinition(InvalidRuleDefinitionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", Instant.now().toString(),
                "error", "INVALID_RULE_DEFINITION",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", Instant.now().toString(),
                "error", "INTERNAL_SERVER_ERROR",
                "message", ex.getMessage()
        ));
    }
}