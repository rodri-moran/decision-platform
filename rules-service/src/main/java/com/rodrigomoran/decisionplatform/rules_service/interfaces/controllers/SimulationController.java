package com.rodrigomoran.decisionplatform.rules_service.interfaces.controllers;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleSimulationService;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleSimulationRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleSimulationResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/simulation")
public class SimulationController {
    private final RuleSimulationService ruleSimulationService;
    @PostMapping()
    public ResponseEntity<RuleSimulationResultDTO> simulateDefinition(@RequestBody RuleSimulationRequestDTO request, @RequestHeader("X-Trace-Id") String traceId){
        return ResponseEntity.ok(ruleSimulationService.simulateDefinition(request, traceId));
    }
}