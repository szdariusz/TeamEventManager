FROM eclipse-temurin:latest
VOLUME /tmp
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT  ["java","-jar","/app.jar","--spring.profiles.active=prod"]
