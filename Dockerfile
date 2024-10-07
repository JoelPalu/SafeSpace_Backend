FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

FROM mysql:latest

LABEL authors="Kirill,Teemu,Miro,Sara"

COPY --from=build /app/target/SafeSpaceAPI.jar /app/SafeSpaceAPI.jar


CMD ["java", "-jar", "target/SafeSpaceAPI.jar"]
