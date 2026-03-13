package com.rodrigomoran.decisionplatform.rules_service.interfaces.dtos;
import java.util.List;
public record PageResponseDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {}