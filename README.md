# Containerizing a Java Microservice

This lab packages a small Java HTTP microservice in Docker so it runs consistently across developer and production environments. The service uses only Java's built-in HTTP server, so it has no external library dependencies.

## Service endpoints

- `GET /` returns service information as JSON.
- `GET /health` returns `{"status":"healthy"}`.
- Other paths return a JSON 404 response.

The service listens on port `8000` by default. Set the `PORT` environment variable to use another port.

## Run it in VS Code

1. Install Docker Desktop and start it. Install the **Extension Pack for Java** in VS Code for Java editing support. The **Docker** extension is optional.
2. In VS Code, choose **File → Open Folder…** and open this project folder.
3. Open **Terminal → New Terminal** and confirm Docker is ready:

   ```sh
   docker --version
   docker info
   ```

4. Build the Docker image in the folder containing the `Dockerfile`:

   ```sh
   docker build -t java-microservice:1.0 .
   ```

   Docker compiles `Main.java` in the JDK build stage and copies the compiled class into a smaller JRE image.

5. Run the container and map port 8000 on your computer to port 8000 in the container:

   ```sh
   docker run --name java-microservice -d -p 8000:8000 java-microservice:1.0
   ```

6. Verify it from another VS Code terminal:

   ```sh
   docker ps
   curl http://localhost:8000/
   curl http://localhost:8000/health
   docker logs java-microservice
   ```

   The health endpoint should return `{"status":"healthy"}`. Inspect the built image with `docker images java-microservice`.

7. Stop and remove the container when finished:

   ```sh
   docker stop java-microservice
   docker rm java-microservice
   ```

If port 8000 is already in use, map another host port, such as `-p 8080:8000`, and open `http://localhost:8080/health`.

## Run without Docker (optional)

Install a JDK 21 or newer. In the VS Code terminal, compile and run:

```sh
javac --add-modules jdk.httpserver Main.java
java --add-modules jdk.httpserver Main
```

Then visit `http://localhost:8000/health`. Stop the service with **Ctrl+C**.
