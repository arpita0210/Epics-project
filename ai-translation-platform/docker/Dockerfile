# Multi-stage Dockerfile for Spring Boot services
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy parent POM and common-lib first for better caching
COPY pom.xml .
COPY common-lib ./common-lib

# Copy the specific service
COPY */pom.xml ./service/
COPY */src ./service/src

# Build the application
RUN mvn clean package -DskipTests -f service/pom.xml

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/service/target/*.jar app.jar

# Expose port (will be overridden by specific service)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
