FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY clientes/pom.xml clientes/pom.xml
COPY clientes/src clientes/src

RUN mvn -f pom.xml -q -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/clientes/target/*.jar app.jar
EXPOSE 8030
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
