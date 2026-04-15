package com.rodrigomoran.decisionplatform.rules_service.application.simulation.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

public enum ComparisonOperator {

    EQUALS {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            return Objects.equals(actualValue, expectedValue);
        }
    },
    NOT_EQUALS {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            return !Objects.equals(actualValue, expectedValue);
        }
    },
    GREATER_THAN {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            return compareAsNumbers(actualValue, expectedValue) > 0;
        }
    },
    GREATER_THAN_OR_EQUALS {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            return compareAsNumbers(actualValue, expectedValue) >= 0;
        }
    },
    LESS_THAN {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            return compareAsNumbers(actualValue, expectedValue) < 0;
        }
    },
    LESS_THAN_OR_EQUALS {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            return compareAsNumbers(actualValue, expectedValue) <= 0;
        }
    },
    CONTAINS {
        @Override
        public boolean evaluate(Object actualValue, Object expectedValue) {
            if (actualValue == null || expectedValue == null) {
                return false;
            }

            if (actualValue instanceof String text) {
                return text.contains(String.valueOf(expectedValue));
            }

            if (actualValue instanceof Collection<?> collection) {
                return collection.contains(expectedValue);
            }

            return false;
        }
    };

    public abstract boolean evaluate(Object actualValue, Object expectedValue);

    public static ComparisonOperator fromName(String operator) {
        if (operator == null || operator.isBlank()) {
            throw new IllegalArgumentException("Operator must not be null or blank");
        }

        return switch (operator.toUpperCase()) {
            case "EQUALS" -> EQUALS;
            case "NOT_EQUALS" -> NOT_EQUALS;
            case "GREATER_THAN" -> GREATER_THAN;
            case "GREATER_THAN_OR_EQUALS" -> GREATER_THAN_OR_EQUALS;
            case "LESS_THAN" -> LESS_THAN;
            case "LESS_THAN_OR_EQUALS" -> LESS_THAN_OR_EQUALS;
            case "CONTAINS" -> CONTAINS;
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

    private static int compareAsNumbers(Object actualValue, Object expectedValue) {
        if (actualValue == null || expectedValue == null) {
            throw new IllegalArgumentException("Numeric comparison values must not be null");
        }

        BigDecimal actual = new BigDecimal(String.valueOf(actualValue));
        BigDecimal expected = new BigDecimal(String.valueOf(expectedValue));

        return actual.compareTo(expected);
    }
}