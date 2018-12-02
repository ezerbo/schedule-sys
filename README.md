# ScheduleSys, A shift scheduling system
This is a system that helps track employees' shifts at multiple companies

## Building the app

### Using maven
Use the maven wrapper included with the project to build it as follows

```
./mvnw clean install
```
Convenience scripts are also available (build & start the app)

[Building (skip the tests)](build.sh)

```
./build.sh
```
[Building and running (skip tests)](run.sh)

```
./run.sh
```

### Using Docker

Building an image
```
docker build -t schedulesys:latest .
```

Running image produced by previous step
```
docker run -d --rm --name schedulesys -p 8080:8080 schedulesys:latest
```

### Using Docker Compose
Run the following command and open your browser to http://localhost:1080 (SMTP server GUI) and http://localhost:8080 (ScheduleSys)
```
docker-compose up
```