FROM openjdk:21
RUN mkdir "/app"
COPY /build/libs/app.jar /app/app.jar

# Create a shell script
RUN echo $'#!/bin/sh \n\
echo "JVM options: $JVM_OPTS" \n\
exec java $JVM_OPTS -jar /app/app.jar' \
> /app/entrypoint.sh \
&& chmod +x /app/entrypoint.sh

EXPOSE 8080

# Use the shell script as entry point
ENTRYPOINT exec /app/entrypoint.sh
