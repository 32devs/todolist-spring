#!/bin/bash                                                                                                                                                                       10,70         All

APP_URL="http://localhost:10001/hello" # 스프링 부트 확인 주소
MAX_ATTEMPTS=10 # 최대 시도 횟수
SLEEP_SECONDS=10 # 각 시도 사이의 대기 시간 (초)

attempt=0
echo "----- check spring boot start -----"
while [ $attempt -lt $MAX_ATTEMPTS ]; do
    response_code=$(curl --write-out "%{http_code}\n" --silent --output /dev/null $APP_URL)

    if [ "$response_code" -eq 200 ]; then
        echo "Spring Boot application is running successfully."
        exit 0
    else
        echo "Attempt $((attempt + 1)): Spring Boot application did not start successfully. HTTP Status Code: $response_code"
        sleep $SLEEP_SECONDS
        ((attempt++))
    fi
done

echo "Error: Unable to verify that the Spring Boot application started successfully after $MAX_ATTEMPTS attempts."
exit 1
