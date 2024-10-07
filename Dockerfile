FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

FROM openjdk:17.0.1-jdk-slim

ENV MYSQL_DATABASE=g2_safespace
ENV MYSQL_USER=pepega
ENV MYSQL_PASSWORD=clap
ENV MYSQL_ROOT_PASSWORD=toor

EXPOSE 8080

LABEL authors="Kirill,Teemu,Miro,Sara"

COPY --from=build /app/target/SafeSpaceAPI.jar /app/SafeSpaceAPI.jar


CMD ["java", "-jar", "/app/SafeSpaceAPI.jar"]
