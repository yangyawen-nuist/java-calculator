package com.calculator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public final class CalculatorServer {
    private static final int DEFAULT_PORT = 8000;
    private static final Calculator CALCULATOR = new Calculator();

    private CalculatorServer() {
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", String.valueOf(DEFAULT_PORT)));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/api/calculate", CalculatorServer::handleCalculate);
        server.createContext("/health", CalculatorServer::handleHealth);
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();
        System.out.printf("Calculator API started on port %d%n", port);
    }

    private static void handleCalculate(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }

        try {
            Map<String, String> query = parseQuery(exchange.getRequestURI().getRawQuery());
            String left = required(query, "left");
            String operator = required(query, "operator");
            String right = required(query, "right");
            double result = CALCULATOR.eval(left, operator, right);

            String body = String.format(
                    "{\"left\":%s,\"operator\":\"%s\",\"right\":%s,\"result\":%s}",
                    formatNumber(Double.parseDouble(left)),
                    escapeJson(operator),
                    formatNumber(Double.parseDouble(right)),
                    formatNumber(result));
            sendJson(exchange, 200, body);
        } catch (ArithmeticException exception) {
            sendJson(exchange, 400, "{\"error\":\"Division by zero\"}");
        } catch (IllegalArgumentException exception) {
            sendJson(exchange, 400, "{\"error\":\"" + escapeJson(exception.getMessage()) + "\"}");
        }
    }

    private static void handleHealth(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, "{\"status\":\"UP\"}");
    }

    private static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> values = new HashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) {
            return values;
        }

        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = decode(parts[0]);
            String value = parts.length == 2 ? decode(parts[1]) : "";
            values.put(key, value);
        }
        return values;
    }

    private static String required(Map<String, String> query, String key) {
        String value = query.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing query parameter: " + key);
        }
        return value;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String formatNumber(double value) {
        if (value == Math.rint(value)) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "Invalid input";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
