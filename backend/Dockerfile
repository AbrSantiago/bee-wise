# Use official Gradle image with JDK 17 for building the project
FROM gradle:8.3.3-jdk17 AS build

# Set working directory inside the container
WORKDIR /home/gradle/project

# Copy Gradle build files
COPY backend/build.gradle backend/settings.gradle ./
COPY backend/gradle ./gradle
COPY backend/src ./src

# Build the jar without running tests
RUN gradle bootJar -x test

# Second stage: use a lighter image to run the application
FROM eclipse-temurin:17-jre-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /home/gradle/project/build/libs/bee-wise-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot will use
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","app.jar"]
