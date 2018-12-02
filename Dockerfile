# Client App
FROM java:alpine
LABEL authors="Edouard ZERBO"
RUN adduser -S -H -D -s /bin/sh schedulesys
WORKDIR /app
COPY ["target/schedule-sys.war", "./"]
USER schedulesys
EXPOSE 8080
CMD [ "-jar", "schedule-sys.war", "--spring.profiles.active=dev"]
ENTRYPOINT [ "java" ]