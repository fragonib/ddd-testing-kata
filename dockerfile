FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
RUN mkdir -p /app/
ARG JAR_FILE=app/build/libs/\*.jar
COPY ${JAR_FILE} /app
ENTRYPOINT ["java", "-jar", "/app/app-0.0.1-SNAPSHOT.jar"]