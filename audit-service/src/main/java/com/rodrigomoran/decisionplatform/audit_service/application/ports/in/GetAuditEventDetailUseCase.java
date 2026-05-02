package com.rodrigomoran.decisionplatform.audit_service.application.ports.in;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditDetailResponse;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditResponse;
import java.util.List;
import java.util.UUID;
public interface GetAuditEventDetailUseCase {
    AuditDetailResponse getById(UUID id);
    List<AuditResponse> getByTraceId(String traceId);
}