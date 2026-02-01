# Stage 1: Build the JAR
FROM eclipse-temurin:21-jdk AS build

# Set working directory
WORKDIR /app

# Copy Gradle/Maven wrapper and build files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies (for Maven)
RUN ./mvnw dependency:go-offline -B

# Copy all source code
COPY src ./src

# Build the application JAR
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jre

WORKDIR /
COPY .env .env
# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar chat-session-mgmt.jar

# Expose port your Spring Boot app runs on
EXPOSE 8081

# Command to run the JAR
ENTRYPOINT ["java","-jar","chat-session-mgmt.jar"]
