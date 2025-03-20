FROM openjdk:21-jdk-slim

WORKDIR /app

EXPOSE 8080

COPY target/*.jar app.jar

COPY src/main/resources/.env .env

RUN apt-get update && apt-get install -y bash

ENTRYPOINT ["bash", "-c", "set -a && . ./.env && set +a && java -jar app.jar"]