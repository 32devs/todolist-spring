FROM openjdk:21
RUN mkdir "/app"
COPY /build/libs/app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILE -DdatabaseUsername=$databaseUsername -DdatabasePassword=$databasePassword -jar /app/app.jar
