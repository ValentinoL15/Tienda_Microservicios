FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/carrito_service-0.0.1.jar
COPY ${JAR_FILE} app_carrito.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app_carrito.jar"]

ENV DB_URL jdbc:mysql://carrito-db:3306/tienda_carrito?createDatabaseIfNotExist=true&serverTimezone=UTC
ENV DB_PASSWORD admin
ENV DB_USER root