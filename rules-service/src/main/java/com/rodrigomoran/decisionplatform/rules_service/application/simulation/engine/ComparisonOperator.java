package com.rodrigomoran.decisionplatform.rules_service.application.simulation.engine;

import java.math.BigDecimal;
import java.util.Objects;

public enum ComparisonOperator {

    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_OR_EQUALS(">="),
    LESS_OR_EQUALS("<=");

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public static ComparisonOperator fromSymbol(String symbol) {
        for (ComparisonOperator operator : values()) {
            if (operator.symbol.equals(symbol)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + symbol);
    }

    public boolean evaluate(Object actualValue, Object expectedValue) {
        return switch (this) {
            case EQUALS -> Objects.equals(actualValue, expectedValue);
            case NOT_EQUALS -> !Objects.equals(actualValue, expectedValue);
            case GREATER_THAN -> compareNumbers(actualValue, expectedValue) > 0;
            case LESS_THAN -> compareNumbers(actualValue, expectedValue) < 0;
            case GREATER_OR_EQUALS -> compareNumbers(actualValue, expectedValue) >= 0;
            case LESS_OR_EQUALS -> compareNumbers(actualValue, expectedValue) <= 0;
        };
    }

    private int compareNumbers(Object actualValue, Object expectedValue) {
        BigDecimal actual = toBigDecimal(actualValue);
        BigDecimal expected = toBigDecimal(expectedValue);
        return actual.compareTo(expected);
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot compare null numeric value");
        }

        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }

        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Value is not numeric: " + value);
        }
    }
}