package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;

import java.util.List;

public record RuleSimulationResultDTO(boolean matched,
                                      List<String> reasons,
                                      String traceId) {
}