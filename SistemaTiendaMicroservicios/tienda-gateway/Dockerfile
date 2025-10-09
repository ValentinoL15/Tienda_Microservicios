FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/tienda-gateway-0.0.1.jar
COPY ${JAR_FILE} app_gateway.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app_gateway.jar"]