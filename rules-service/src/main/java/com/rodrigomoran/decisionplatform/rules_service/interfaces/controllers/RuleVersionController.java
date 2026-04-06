package com.rodrigomoran.decisionplatform.rules_service.interfaces.controllers;

import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RulePublishService;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleVersionCommandService;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleVersionQueryService;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.PageResponseDto;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RuleVersionController {
   private final RulePublishService rulePublishService;
   private final RuleVersionCommandService ruleVersionCommandService;
   private final RuleVersionQueryService ruleVersionQueryService;

   @PostMapping("/{draftId}/public")
    public ResponseEntity<RuleVersionResponseDTO> publishDraft(@PathVariable Long draftId,
                                                               @RequestHeader("X-Actor") String actor,
                                                               @RequestHeader("X-Trace-Id") String traceId,
                                                               @RequestHeader("X-Idempotency-Key") String idempotencyKey)
   {
       return ResponseEntity.ok(rulePublishService.publishDraft(draftId,actor,traceId,idempotencyKey));
   }
   @PostMapping("/{ruleKey}/{targetVersionNumber}/rollback")
    public ResponseEntity<RuleVersionResponseDTO> rollbackToVersion(@PathVariable Integer targetVersionNumber,
                                                                    @PathVariable String ruleKey,
                                                                    @RequestHeader("X-Actor") String actor,
                                                                    @RequestHeader("X-Trace-Id") String traceId,
                                                                    @RequestHeader("X-Idempotency-Key") String idempotencyKey){
       return ResponseEntity.ok(ruleVersionCommandService.rollbackToVersion(ruleKey, targetVersionNumber,actor, traceId, idempotencyKey));
   }
   @GetMapping("/{ruleKey}/active")
    public ResponseEntity<RuleVersionResponseDTO> getActiveVersion(@PathVariable String ruleKey)
   {
       return ResponseEntity.ok(ruleVersionQueryService.getActiveVersion(ruleKey));
   }
   @GetMapping("/{ruleKey}/version/{versionNumber}")
    public ResponseEntity<RuleVersionResponseDTO> getVersion(@PathVariable String ruleKey,
                                                             @PathVariable Integer versionNumber){
       return ResponseEntity.ok(ruleVersionQueryService.getVersion(ruleKey,versionNumber));
   }
   @GetMapping("/{ruleKey}/versions")
    public ResponseEntity<PageResponseDto<RuleVersionResponseDTO>> listVersions(@PathVariable String ruleKey,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size){
       return ResponseEntity.ok(ruleVersionQueryService.listVersions(ruleKey, page, size));
   }
}