FROM openjdk:21
RUN mkdir "/app"
COPY /build/libs/app.jar /app/app.jar

EXPOSE 8080
CMD ["java","-Dspring.profiles.active=${SPRING_PROFILE}","-jar","/app/app.jar"]
