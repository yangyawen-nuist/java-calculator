# Stage 1 uses Maven and JDK 17 to compile, test, and package the application.
FROM m.daocloud.io/docker.io/library/maven:3.8.3-openjdk-17 AS build

# Set the build working directory inside the container.
WORKDIR /app

# Copy the Maven project descriptor first to improve dependency-layer caching.
COPY pom.xml .

# Download Maven dependencies before copying source code.
RUN mvn -B dependency:go-offline

# Copy the application source code into the build stage.
COPY src ./src

# Run all tests and create the executable calculator JAR.
RUN mvn -B clean package

# Stage 2 uses a lightweight Java 17 JRE for the final runtime image.
FROM m.daocloud.io/docker.io/library/eclipse-temurin:17-jre-jammy AS runtime

# Set the runtime working directory inside the container.
WORKDIR /app

# Copy only the executable JAR from the build stage.
COPY --from=build /app/target/calculator.jar ./calculator.jar

# Run the calculator and accept three arguments after the image name.
ENTRYPOINT ["java", "-jar", "calculator.jar"]
