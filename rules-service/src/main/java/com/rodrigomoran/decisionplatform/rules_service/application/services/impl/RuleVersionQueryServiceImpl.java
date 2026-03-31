package com.rodrigomoran.decisionplatform.rules_service.application.services.impl;
import com.rodrigomoran.decisionplatform.rules_service.application.mappers.RuleVersionMapper;
import com.rodrigomoran.decisionplatform.rules_service.application.services.interfaces.RuleVersionQueryService;
import com.rodrigomoran.decisionplatform.rules_service.domain.enums.RuleVersionStatus;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.entities.RuleVersion;
import com.rodrigomoran.decisionplatform.rules_service.infraestructure.persistence.repositories.RuleVersionRepository;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.PageResponseDto;
import com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos.RuleVersionResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RuleVersionQueryServiceImpl implements RuleVersionQueryService {
    private final RuleVersionRepository ruleVersionRepository;
    private final RuleVersionMapper ruleVersionMapper;
    @Override
    public RuleVersionResponseDTO getActiveVersion(String ruleKey) {
        return ruleVersionMapper.toResponseDto(ruleVersionRepository.findByRuleKeyAndStatus(ruleKey, RuleVersionStatus.ACTIVE)
                .orElseThrow(EntityNotFoundException::new));
    }
    @Override
    public RuleVersionResponseDTO getVersion(String ruleKey, Integer versionNumber) {
        return ruleVersionMapper.toResponseDto(ruleVersionRepository.findByRuleKeyAndVersionNumber(ruleKey, versionNumber)
                .orElseThrow(() -> new EntityNotFoundException("RuleVersion with ruleKey " + ruleKey + " and versionNumber " + versionNumber + " not found.")));
    }
    @Override
    public PageResponseDto<RuleVersionResponseDTO> listVersions(String ruleKey, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RuleVersion> result;

        if(ruleKey != null) {
            result = ruleVersionRepository.findAllByRuleKey(ruleKey, pageable);
        } else {
            result = ruleVersionRepository.findAll(pageable);
        }

        List<RuleVersionResponseDTO> content =
                result.getContent()
                        .stream()
                        .map(ruleVersionMapper::toResponseDto)
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