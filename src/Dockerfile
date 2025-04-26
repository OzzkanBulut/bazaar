# Use a Java runtime as base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar file into the container
COPY build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
