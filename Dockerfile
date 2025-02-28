FROM maven:3.8.5-openjdk-17 AS builder

ENV DOCKER_HOST=tcp://localhost:2375

WORKDIR /home/app
COPY pom.xml pom.xml
COPY ./src ./src
RUN mvn package

FROM openjdk:18-alpine
WORKDIR /home/app
COPY --from=builder /home/app/target/magagement-0.0.1-SNAPSHOT.jar ./magagement-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "magagement-0.0.1-SNAPSHOT.jar"]