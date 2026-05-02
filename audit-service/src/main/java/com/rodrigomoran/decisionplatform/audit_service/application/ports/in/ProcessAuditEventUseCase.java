package com.rodrigomoran.decisionplatform.audit_service.application.ports.in;

import com.rodrigomoran.decisionplatform.audit_service.application.dto.DecisionMadeAuditMessage;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.RulePublishedAuditMessage;

public interface ProcessAuditEventUseCase {
    void processDecisionMadeEvent(DecisionMadeAuditMessage message);
    void processRulePublishedEvent(RulePublishedAuditMessage message);
}