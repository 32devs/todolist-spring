#!/bin/bash

export JAVA_OPTS="$JAVA_OPTS \
                  -server \
                  -Dspring.profiles.active="${SPRING_PROFILE}" \
"
