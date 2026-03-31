package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;
import com.rodrigomoran.decisionplatform.rules_service.application.mappers.RuleDraftMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleDraftQueryService;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleDraftStatus;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleDraftByRuleKeyNotFoundException;
import com.rodrigomoran.decisionplatform.rules_service.domain.exceptions.RuleDraftNotFoundException;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleDraft;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.RuleDraftRepository;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.PageResponseDto;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleDraftResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RuleDraftQueryServiceImpl implements RuleDraftQueryService {
    private final RuleDraftRepository ruleDraftRepository;
    private final RuleDraftMapper mapper;

    @Override
    public RuleDraftResponseDTO getDraftById(Long draftId) {
        RuleDraft ruleDraft = ruleDraftRepository.findById(draftId)
                .orElseThrow(() -> new RuleDraftNotFoundException(draftId));
        return mapper.toResponseDTO(ruleDraft);
    }

    @Override
    public RuleDraftResponseDTO getDraftByRuleKey(String ruleKey) {
        RuleDraft ruleDraft = ruleDraftRepository.findByRuleKey(ruleKey)
                .orElseThrow(() -> new RuleDraftByRuleKeyNotFoundException(ruleKey));
        return mapper.toResponseDTO(ruleDraft);
    }

    @Override
    public PageResponseDto<RuleDraftResponseDTO> listDrafts(RuleDraftStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<RuleDraft> result;

        if(status != null){
            result = ruleDraftRepository.findByStatus(status, pageable);
        } else{
            result = ruleDraftRepository.findAll(pageable);
        }
        List<RuleDraftResponseDTO> content =
                result.getContent()
                        .stream()
                        .map(mapper::toResponseDTO)
                        .toList();

        return new PageResponseDto<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
    }
}