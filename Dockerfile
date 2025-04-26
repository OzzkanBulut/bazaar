# Use Java 21 runtime as base image
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy the .jar file from the target folder (correct directory for Maven)
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
