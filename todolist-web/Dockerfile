FROM openjdk:21

RUN mkdir "/app"
COPY /build/libs/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT exec java $JVM_OPTS -jar /app/app.jar
