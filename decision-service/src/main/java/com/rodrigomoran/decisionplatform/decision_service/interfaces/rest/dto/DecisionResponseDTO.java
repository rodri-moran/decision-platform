package com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.dto;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
public class DecisionResponseDTO {

    private boolean result;
    private List<String> reasons = new ArrayList<>();
    private String ruleKey;
    private Integer version;
    private String traceId;
    private Instant evaluatedAt;
}