package com.rodrigomoran.decisionplatform.decision_service.interfaces.rest.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluateDecisionRequestDTO {

    private String ruleKey;
    @Builder.Default
    private Map<String, Object> data = new HashMap<>();
}