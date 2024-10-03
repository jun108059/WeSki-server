FROM openjdk:17-jdk-slim

EXPOSE 8080

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} weski-app.jar

CMD java -Dserver.port=8080 \
        -Dspring.datasource.username=master \
        -Dspring.datasource.password=master \
        -jar /weski-app.jar
