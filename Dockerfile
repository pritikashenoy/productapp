# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file built by Maven or Gradle into the container
COPY target/product_listing_app-0.0.1-SNAPSHOT.jar /app/product-listing-app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file when the container starts
ENTRYPOINT ["java", "-jar", "product-listing-app.jar"]
