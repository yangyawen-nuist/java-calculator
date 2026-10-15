package com.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator c = new Calculator();

    @Test
    void addWorks() {
        assertEquals(4.0, c.eval("2", "+", "2"));
    }

    @Test
    void subtractWorks() {
        assertEquals(3.0, c.eval("7", "-", "4"));
    }

    @Test
    void multiplyWorks() {
        assertEquals(15.0, c.eval("3", "*", "5"));
    }

    @Test
    void divideWorks() {
        assertEquals(2.0, c.eval("10", "/", "5"));
    }

    @Test
    void divideByZeroThrows() {
        assertThrows(ArithmeticException.class,
                () -> c.eval("10", "/", "0"));
    }

    @Test
    void blankFirstOperandThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> c.eval("", "+", "2"));
    }

    @Test
    void blankSecondOperandThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> c.eval("2", "+", " "));
    }

    @Test
    void unknownOperatorThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> c.eval("2", "^", "3"));
    }

    @ParameterizedTest(name = "{0} {1} {2} = {3}")
    @CsvSource({
            "2, +, 2, 4",
            "10, /, 5, 2",
            "7, -, 4, 3",
            "3, *, 5, 15",
            "-6, +, 2, -4",
            "0, *, 9, 0"
    })
    void arithmeticWorks(String a, String op, String b, double expected) {
        assertEquals(expected, c.eval(a, op, b));
    }
}
