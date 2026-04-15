package com.rodrigomoran.decisionplatform.decision_service.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionResult {
    private boolean result;
    private List<String> reasons = new ArrayList<>();
    private String ruleKey;
    private Integer version;
    private String traceId;
    private Instant evaluatedAt;
}