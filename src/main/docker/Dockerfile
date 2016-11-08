FROM java:openjdk-8-alpine
VOLUME /tmp
ADD schedule-sys-1.0.0-SNAPSHOT.jar schedule-sys.jar
RUN sh -c 'touch /schedule-sys.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/schedule-sys.jar"]