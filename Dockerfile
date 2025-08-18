# Build stage (JDK 17 + Maven)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY . .
RUN mvn -q -DskipTests clean package


FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} /app/app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport"
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

