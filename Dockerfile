FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

FROM openjdk:17.0.1-jdk-slim

EXPOSE 8080

LABEL authors="Kirill,Teemu,Miro,Sara"

COPY --from=build /app/target/SafeSpaceAPI.jar /app/SafeSpaceAPI.jar

CMD ["java", "-jar", "/app/SafeSpaceAPI.jar"]
