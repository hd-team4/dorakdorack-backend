FROM eclipse-temurin:21-jdk

ARG JAR_FILE

RUN mkdir -p /app/wallet

WORKDIR /app
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
