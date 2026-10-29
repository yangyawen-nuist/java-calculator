# Stage 1 uses Maven and JDK 17 to compile, test, and package the backend.
FROM m.daocloud.io/docker.io/library/maven:3.8.3-openjdk-17 AS build

# Set the build working directory inside the container.
WORKDIR /app

# Copy the container-specific Maven mirror configuration for reliable downloads.
COPY settings.xml .

# Copy the Maven descriptor first so dependency downloads can be cached.
COPY pom.xml .

# Download Maven dependencies before copying frequently changed source files.
RUN mvn -B -s settings.xml dependency:go-offline

# Copy the backend source code into the build stage.
COPY src ./src

# Run all JUnit tests and create the executable backend JAR.
RUN mvn -B -s settings.xml clean package

# Stage 2 uses a smaller Java 17 runtime image.
FROM m.daocloud.io/docker.io/library/eclipse-temurin:17-jre-jammy AS runtime

# Set the backend runtime working directory.
WORKDIR /app

# Copy only the packaged JAR from the build stage.
COPY --from=build /app/target/calculator.jar ./calculator.jar

# Document the port used by the calculator HTTP API.
EXPOSE 8000

# Start the calculator API when the container launches.
ENTRYPOINT ["java", "-jar", "calculator.jar"]
