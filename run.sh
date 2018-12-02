#!/bin/bash
./build.sh
STATUS=$?
if [ $STATUS -eq 0 ]; then
    java -jar target/schedule-sys.war --spring.profiles.active=dev
else
    echo "Unable to run application"
fi
