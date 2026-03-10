package com.rodrigomoran.decisionplatform.rules_service.application.simulation.model;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContextValueResolver {

    public Object resolve(Map<String, Object> context, String path) {
        if (context == null || path == null || path.isBlank()) {
            return null;
        }

        String[] parts = path.split("\\.");
        Object current = context;

        for (String part : parts) {
            if (!(current instanceof Map<?, ?> currentMap)) {
                return null;
            }

            current = currentMap.get(part);

            if (current == null) {
                return null;
            }
        }

        return current;
    }
}