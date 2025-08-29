FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .
RUN apt-get update && apt-get install -y maven && mvn dependency:go-offline

COPY src ./src

RUN mvn clean package

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/demo-project-0.0.1-SNAPSHOT.jar"] 