package com.calculator;

import org.apache.commons.lang3.StringUtils;

public class Calculator {
    public double eval(String a, String op, String b) {
        if (StringUtils.isBlank(a) || StringUtils.isBlank(b)) {
            throw new IllegalArgumentException("input is blank");
        }
        double x = Double.parseDouble(a);
        double y = Double.parseDouble(b);
        return switch (op) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*", "x", "X" -> x * y;
            case "/" -> {
                if (y == 0) throw new ArithmeticException("division by zero");
                yield x / y;
            }
            default -> throw new IllegalArgumentException(op);
        };
    }
}
