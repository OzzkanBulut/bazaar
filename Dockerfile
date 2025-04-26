# Use Java 21 runtime as base image
FROM eclipse-temurin:21-jdk

# Set working directory inside the container
WORKDIR /app

# Install Maven (you need Maven to build your project inside the Docker container)
RUN apt-get update && apt-get install -y maven

# Copy your project files into the Docker container
COPY . /app

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build your project and skip tests (this will generate the .jar file inside /app/target/)
RUN ./mvnw clean package -DskipTests

# Expose port 8080 for your app
EXPOSE 8080

# Run the built .jar file from target/
ENTRYPOINT ["java", "-jar", "target/YOUR_JAR_NAME.jar"]
