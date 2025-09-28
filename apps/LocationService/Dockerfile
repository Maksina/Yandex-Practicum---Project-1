FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/LocationService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]