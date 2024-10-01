# Use a base image that includes Java and a minimal OS
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the generated jar file from the build context to the container
COPY target/currencyconverter-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the Spring Boot application is running on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
