FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/products_service-0.0.1.jar
COPY ${JAR_FILE} app_products.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_products.jar"]

ENV DB_URL jdbc:mysql://products-db:3306/tienda_products?createDatabaseIfNotExist=true&serverTimezone=UTC
ENV DB_PASSWORD admin
ENV DB_USER root