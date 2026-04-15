package com.rodrigomoran.decisionplatform.decision_service.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationContext {
    private Map<String, Object> data = new HashMap<>();
}