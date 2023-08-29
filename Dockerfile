FROM eclipse-temurin:17-jdk
VOLUME /tmp
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT  ["java","-jar","/app.jar","--spring.profiles.active=prod"]


## This part works only with turned off Zscaler :(

#FROM gradle:7.5-jdk-alpine AS build
#WORKDIR /build
#COPY . .
#RUN gradle build
#
#FROM eclipse-temurin:17-jdk
#VOLUME /tmp
#ARG JAR_FILE=/build/build/libs/*.jar
#COPY --from=build ${JAR_FILE} app.jar
#EXPOSE 8080
#ENTRYPOINT  ["java","-jar","/app.jar","--spring.profiles.active=prod"]
