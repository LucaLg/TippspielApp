FROM openjdk:11
LABEL maintainer="tippspielcontainer"
COPY target/tippspiel.jar  app.jar
ENTRYPOINT ["java","-jar","app.jar"]
