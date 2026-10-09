package com.school.project1.backend.clients;

import com.school.project1.backend.dto.RoutePlanRequestDto;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OtpClientTest {

  private final JsonMapper jsonMapper = JsonMapper.builder().build();
  private final AtomicReference<String> requestMethod = new AtomicReference<>();
  private final AtomicReference<String> requestPath = new AtomicReference<>();
  private final AtomicReference<String> requestBody = new AtomicReference<>();
  private final AtomicReference<String> responseBody = new AtomicReference<>();

  private HttpServer server;
  private OtpClient otpClient;

  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/otp/gtfs/v1", exchange -> {
      requestMethod.set(exchange.getRequestMethod());
      requestPath.set(exchange.getRequestURI().getPath());
      requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));

      byte[] body = responseBody.get().getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(200, body.length);
      try (var output = exchange.getResponseBody()) {
        output.write(body);
      }
    });
    server.start();
    otpClient = new OtpClient(
      RestClient.builder(),
      "http://127.0.0.1:" + server.getAddress().getPort()
    );
  }

  @AfterEach
  void stopServer() {
    server.stop(0);
  }

  @Test
  void planRoutePostsQueryAndStationIds() {
    responseBody.set("""
      {"data":{"planConnection":{"edges":[]}}}
      """);

    var response = otpClient.planRoute(
      new RoutePlanRequestDto("NL:station:1", "NL:station:2")
    );

    JsonNode body = jsonMapper.readTree(requestBody.get());
    assertEquals("POST", requestMethod.get());
    assertEquals("/otp/gtfs/v1", requestPath.get());
    assertTrue(body.path("query").stringValue().contains("planConnection"));
    assertEquals("NL:station:1", body.path("variables").path("originStationId").stringValue());
    assertEquals("NL:station:2", body.path("variables").path("destinationStationId").stringValue());
    assertNotNull(response.data().planConnection());
  }

  @Test
  void searchStationsPostsQueryAndName() {
    responseBody.set("""
      {"data":{"stations":[]}}
      """);

    var response = otpClient.searchStations("Utrecht");

    JsonNode body = jsonMapper.readTree(requestBody.get());
    assertEquals("POST", requestMethod.get());
    assertEquals("/otp/gtfs/v1", requestPath.get());
    assertTrue(body.path("query").stringValue().contains("stations(name: $name)"));
    assertEquals("Utrecht", body.path("variables").path("name").stringValue());
    assertNotNull(response.data().stations());
  }
}
