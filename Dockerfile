FROM eclipse-temurin:21-jdk

WORKDIR /app

EXPOSE 8080

COPY target/*.jar app.jar

COPY .env.uat secrets/.env

ENV TZ=Asia/Ho_Chi_Minh

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.import=file:/app/secrets/.env[.properties]"]