FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ARG APPLICATION_PROPERTIES
ADD ${JAR_FILE} app.jar
ADD ${APPLICATION_PROPERTIES} application.properties
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
