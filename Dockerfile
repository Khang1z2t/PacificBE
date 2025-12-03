FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

EXPOSE 8080

COPY target/*.jar app.jar

COPY .env.uat .env

RUN apt-get update && apt-get install -y bash

ENTRYPOINT ["java", "-jar", "app.jar"]