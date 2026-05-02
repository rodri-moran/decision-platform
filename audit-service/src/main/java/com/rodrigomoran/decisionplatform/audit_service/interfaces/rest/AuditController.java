package com.rodrigomoran.decisionplatform.audit_service.interfaces.rest;

import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditDetailResponse;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditResponse;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditSearchRequest;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.GetAuditEventDetailUseCase;
import com.rodrigomoran.decisionplatform.audit_service.application.ports.in.SearchAuditEventsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
@Validated
public class AuditController {

    private final SearchAuditEventsUseCase searchAuditEventsUseCase;
    private final GetAuditEventDetailUseCase getAuditEventDetailUseCase;

    @GetMapping
    public ResponseEntity<Page<AuditResponse>> searchAudits(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String aggregateType,
            @RequestParam(required = false) String aggregateId,
            @RequestParam(required = false) String traceId,
            @RequestParam(required = false) String decision,
            @RequestParam(required = false) Instant fromTimestamp,
            @RequestParam(required = false) Instant toTimestamp,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "occurredAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        AuditSearchRequest request = AuditSearchRequest.builder()
                .eventType(eventType)
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .traceId(traceId)
                .decision(decision)
                .fromTimestamp(fromTimestamp)
                .toTimestamp(toTimestamp)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Page<AuditResponse> response = searchAuditEventsUseCase.search(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditDetailResponse> getAuditById(@PathVariable UUID id) {
        AuditDetailResponse response = getAuditEventDetailUseCase.getById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/trace/{traceId}")
    public ResponseEntity<List<AuditResponse>> getByTraceId(@PathVariable String traceId) {
        List<AuditResponse> response = getAuditEventDetailUseCase.getByTraceId(traceId);

        return ResponseEntity.ok(response);
    }
}