package com.calculator;

public class App {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("用法: java com.calculator.App 10 + 5");
            return;
        }

        Calculator c = new Calculator();
        try {
            double result = c.eval(args[0], args[1], args[2]);
            System.out.println("Result = " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: Division by zero");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
