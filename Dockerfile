FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

FROM mysql:latest

ENV MYSQL_DATABASE=g2_safespace
ENV MYSQL_USER=/run/secrets/mysql_user
ENV MYSQL_PASSWORD_FILE=/run/secrets/mysql_user_password
ENV MYSQL_ROOT_PASSWORD_FILE=/run/secrets/mysql_root_password

EXPOSE 3306

LABEL authors="Kirill,Teemu,Miro,Sara"

COPY --from=build /app/target/SafeSpaceAPI.jar /app/SafeSpaceAPI.jar


CMD ["java", "-jar", "target/SafeSpaceAPI.jar"]
