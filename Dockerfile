# Use an official Java runtime as the base image
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the container
COPY . /app
#COPY target/portfolio-app-0.0.1-SNAPSHOT.jar app.jar

# Make mvnw executable in the container
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/portfolio-app-0.0.1-SNAPSHOT.jar"]
