FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD schedule-sys-1.0.0-SNAPSHOT.jar schedulesys.jar
RUN sh -c 'touch /schedulesys.jar'
    
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/schedulesys.jar"]