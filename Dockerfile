FROM openjdk:21
# 스크립트 파일 복사 및 실행 권한 부여
#RUN mkdir "/script"
#COPY /script/health_check.sh /script/health_check.sh
#RUN chmod +x /home/devs32/script/health_check.sh

RUN mkdir "/app"
COPY todolist-web/build/libs/app.jar /app/app.jar

EXPOSE 8080

# 환경 변수 설정을 고려할 수 있음 (필요한 경우)
# ENV APP_URL="http://localhost:10001/hello"
# ENV MAX_ATTEMPTS=10
# ENV SLEEP_SECONDS=10

ENTRYPOINT exec java $JVM_OPTS -jar /app/app.jar
