FROM openjdk:21-jdk-slim

WORKDIR /app

EXPOSE 8080

COPY target/*.jar app.jar

COPY .env .env

RUN apt-get update && apt-get install -y bash

ENTRYPOINT ["java", "-jar", "app.jar"]