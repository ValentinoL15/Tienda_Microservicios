FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/users_service-0.0.1.jar
COPY ${JAR_FILE} app_users.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app_users.jar"]

ENV DB_URL jdbc:mysql://users-db:3306/tienda_users?createDatabaseIfNotExist=true&serverTimezone=UTC
ENV DB_PASSWORD admin
ENV DB_USER root