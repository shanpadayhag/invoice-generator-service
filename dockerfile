FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/mainbe-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# FROM ubuntu:24.04

# RUN apt update
# RUN apt install openjdk-21-jdk -y

# WORKDIR /app
# COPY target/mainbe-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java", "-jar", "app.jar"]
