FROM java:alpine
LABEL authors="Edouard ZERBO"
RUN addgroup ezerbo
RUN adduser -S -H -D -s /bin/sh -G ezerbo ezerbo
WORKDIR /app
COPY ["target/schedule-sys.war", "docker-entrypoint.sh",  "./"]
RUN chmod +x docker-entrypoint.sh
RUN chown ezerbo:ezerbo docker-entrypoint.sh
USER ezerbo:ezerbo
EXPOSE 8080
ENTRYPOINT [ "./docker-entrypoint.sh" ]