package com.rodrigomoran.decisionplatform.rules_service.interfaces.controllers;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleDraftCommandService;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.CreateRuleDraftRequestDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.UpdateRuleDraftRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/draft")
@RequiredArgsConstructor
public class RuleDraftController {
    private final RuleDraftCommandService ruleDraftCommandService;
    @PostMapping()
    public ResponseEntity<RuleDraftResponseDTO> createDraft(
            @RequestBody CreateRuleDraftRequestDTO request,
            @RequestHeader("X-Actor") String actor,
            @RequestHeader("X-Trace-Id") String traceId
            ){
        return ResponseEntity.ok(ruleDraftCommandService.createDraft(request, actor, traceId));
    }
    @PatchMapping("/{draftId}")
    public ResponseEntity<RuleDraftResponseDTO> update(
            @PathVariable Long draftId,
            @RequestBody UpdateRuleDraftRequestDTO request,
            @RequestHeader("X-Actor") String actor,
            @RequestHeader("X-Trace-Id") String traceId) {
        return ResponseEntity.ok(ruleDraftCommandService.updateDraft(draftId, request ,actor ,traceId));
    }
    @PatchMapping("/{draftId}/archive")
    public ResponseEntity<Void> archiveDraft(
            @PathVariable Long draftId,
            @RequestHeader("X-Actor") String actor,
            @RequestHeader("X-Trace-Id") String traceId){
        ruleDraftCommandService.archiveDraft(draftId,actor,traceId);
        return ResponseEntity.noContent().build();
    }
}