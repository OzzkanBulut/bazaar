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

# Build your project (this will generate the .jar file)
RUN ./mvnw clean package

# Copy the generated .jar file from target/ to the container
COPY target/*.jar app.jar

# Expose port 8080 for your app
EXPOSE 8080

# Run the .jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
