FROM eclipse-temurin:21-jre
COPY build/libs/*SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml src/main/resources/application.yml
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
