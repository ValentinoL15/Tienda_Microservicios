FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/users_service-0.0.1.jar
COPY ${JAR_FILE} app_users.jar
ENTRYPOINT ["java", "-jar", "app_users.jar"]