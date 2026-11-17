import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/health", exchange -> respond(exchange, 200,
                "{\"status\":\"healthy\"}"));
        server.createContext("/", exchange -> {
            if (!exchange.getRequestURI().getPath().equals("/")) {
                respond(exchange, 404, "{\"error\":\"not found\"}");
                return;
            }
            respond(exchange, 200,
                    "{\"service\":\"student-microservice\","
                            + "\"message\":\"Hello from a containerized Java microservice!\","
                            + "\"endpoints\":[\"/\",\"/health\"]}");
        });
        server.start();
        System.out.println("Java microservice listening on port " + port);
    }

    private static void respond(HttpExchange exchange, int status, String json) throws IOException {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, body.length);
        try (var output = exchange.getResponseBody()) {
            output.write(body);
        }
    }
}
