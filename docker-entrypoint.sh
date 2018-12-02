#!/bin/sh
set -e
SMTP_HOST="localhost"
APP_PROFILE="dev"
if [ -n "$1" ]; then
 if [ "$1" = "dev" ] || [ "$1" = "prod" ]; then
    APP_PROFILE=$1
 else
    echo "No such profile."
    exit
 fi  
fi
if [ -n "$EMAIL_PORT" ]; then
   SMTP_HOST="email"
fi
echo "Running with Profile:" $APP_PROFILE "SMTP host:" $SMTP_HOST
java -Djava.security.egd=file:/dev/./urandom \
     -Xdebug \
     -Xrunjdwp:server=y,address=8000,transport=dt_socket,suspend=n \
     -jar schedule-sys.war --spring.profiles.active=$APP_PROFILE --spring.mail.host=$SMTP_HOST
exec "$@"