# Compile the Java service in a JDK image.
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src
COPY Main.java .
RUN javac --add-modules jdk.httpserver Main.java

# Run it with a smaller Java runtime image.
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/Main.class .
ENV PORT=8000
EXPOSE 8000
USER 10001
CMD ["java", "--add-modules", "jdk.httpserver", "Main"]
