FROM eclipse-temurin:21-jdk

ARG JAR_FILE

# 컨테이너 내부 경로로 복사
WORKDIR /app
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
