#!/bin/bash
mvn clean install -Pui -DskipTests=true
STATUS=$?
if [ $STATUS -eq 0 ]; then
    java -jar target/schedule-sys.war --spring.profiles.active=dev
else
    echo "Unable to run application"
fi
