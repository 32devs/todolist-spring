FROM openjdk:21
RUN mkdir "/app"
COPY /build/libs/app.jar /app/app.jar

EXPOSE 8080
#CMD echo "$JVM_OPTS"
ENTRYPOINT exec java $JVM_OPTS -jar /app/app.jar
