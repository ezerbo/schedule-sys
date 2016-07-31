FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD schedule-sys-1.0.0-SNAPSHOT.jar schedulesys.jar
RUN sh -c 'touch /schedulesys.jar'

EXPOSE 3306

RUN apk add --update mysql && \
    mkdir -p /var/lib/mysql && \
    mkdir -p /etc/mysql/conf.d && \
    { \
        echo '[mysqld]'; \
        echo 'user = root'; \
        echo 'datadir = /var/lib/mysql'; \
        echo 'port = 3306'; \
        echo 'log-bin = /var/lib/mysql/mysql-bin'; \
        echo 'socket = /var/lib/mysql/mysql.sock'; \
        echo '!includedir /etc/mysql/conf.d/'; \
    } > /etc/mysql/my.cnf && \
    rm -rf /var/cache/apk/*
    
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/schedulesys.jar"]