FROM openjdk:11-jre-slim
MAINTAINER "Docker App <docker@app.com>"
WORKDIR /app

COPY ./target/*.jar ./app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker" ,"-jar", "/app/app.jar"]

EXPOSE 7979
