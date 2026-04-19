# Use Java 17
FROM eclipse-temurin:17-jdk

# Copy jar file
COPY build/libs/*.jar app.jar

# Run app
ENTRYPOINT ["java", "-jar", "/app.jar"]