package com.rodrigomoran.decisionplatform.audit_service.application.ports.in;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditResponse;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.AuditSearchRequest;
import org.springframework.data.domain.Page;
public interface SearchAuditEventsUseCase {
    Page<AuditResponse> search(AuditSearchRequest request);
}