[![Calculator CI](https://github.com/yangyawen-nuist/java-calculator/actions/workflows/ci-calculator.yml/badge.svg)](https://github.com/yangyawen-nuist/java-calculator/actions/workflows/ci-calculator.yml)

# Java Calculator

A Maven-based command-line calculator developed for the software development tools labs.

## Requirements

- JDK 17 or later
- Apache Maven 3.9 or later

## Supported Operations

- Addition
- Subtraction
- Multiplication
- Division with division-by-zero validation

## Run the Tests

```bash
mvn clean test
```

The GitHub Actions workflow runs the JUnit 5 test suite on JDK 17 and JDK 21 for every push and pull request to `main`.
