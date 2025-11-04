FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN mvn clean package

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/fitness-tracker-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]