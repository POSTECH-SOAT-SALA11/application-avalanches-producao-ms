FROM maven:3.8.7-openjdk-18-slim AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:18-jdk-slim

WORKDIR /app

COPY --from=build /app/target/avalanches-0.0.1-SNAPSHOT.jar /app/avalanches-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "avalanches-0.0.1-SNAPSHOT.jar"]