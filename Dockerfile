# Stage 1: Build the application using Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src
# Package the application (skip tests to make deployment faster)
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight Java 17 runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built .jar file from the first stage
COPY --from=build /app/target/*.jar app.jar
# Expose the port Render expects
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]