package com.school.project1.backend.controllers;

import com.sun.net.httpserver.HttpServer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.docker.compose.enabled=false",
        "spring.datasource.generate-unique-name=true"
    }
)
class RouteControllerIntegrationTest {

    private static HttpServer otpServer;
    private static final AtomicReference<String> otpRequest = new AtomicReference<>();
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureOtp(DynamicPropertyRegistry registry) throws IOException {
        otpServer = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        otpServer.createContext("/otp/gtfs/v1", exchange -> {
            otpRequest.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] body = """
                {"data":{"planConnection":{"edges":[{"node":{
                  "start":"2026-10-29T12:00:00+01:00",
                  "end":"2026-10-29T13:00:00+01:00",
                  "legs":[]
                }}]}}}
                """.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (var output = exchange.getResponseBody()) {
                output.write(body);
            }
        });
        otpServer.start();
        registry.add("otp.base-url", () -> "http://127.0.0.1:" + otpServer.getAddress().getPort());
    }

    @BeforeEach
    void resetRequest() {
        otpRequest.set(null);
    }

    @AfterAll
    static void stopOtp() {
        if (otpServer != null) {
            otpServer.stop(0);
        }
    }

    private Response planWithSelection(String selectionJson) {
        return given()
            .baseUri("http://127.0.0.1").port(port)
            .contentType("application/json")
            .body("""
                {
                  "originStationId":"NL:station:1",
                  "destinationStationId":"NL:station:2",
                  "extraTransferMinutes":5%s
                }
                """.formatted(selectionJson == null ? "" : ",\"selectedFacilities\":" + selectionJson))
            .post("/api/routes/plan");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "[\"EXTRA_TRANSFER_TIME\"]", "[\"UNKNOWN_NEED\"]", "null", "[null]",
        "\"ELEVATOR_REQUIRED\"", "{}", "[1]"
    })
    void rejectsInvalidFacilitySelectionsBeforeCallingOtp(String selectionJson) {
        planWithSelection(selectionJson).then().statusCode(400);
        assertThat(otpRequest.get()).isNull();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
        "[]", "[\"ELEVATOR_REQUIRED\"]",
        "[\"ELEVATOR_REQUIRED\",\"ELEVATOR_REQUIRED\",\"GUIDING_LINE_REQUIRED\"]"
    })
    void acceptsValidOrOmittedSelectionsWithoutAddingFacilityArgumentsToOtp(String selectionJson) {
        planWithSelection(selectionJson).then().statusCode(200);
        assertThat(jsonMapper.readTree(otpRequest.get()).path("variables")).isEqualTo(
            jsonMapper.readTree("""
                {
                  "originStationId":"NL:station:1",
                  "destinationStationId":"NL:station:2",
                  "extraTransferTime":"PT5M"
                }
                """)
        );
    }

    @Test
    @Sql(statements = "DELETE FROM \"special_need\" WHERE \"type\" = 'ELEVATOR_REQUIRED'")
    @Sql(
        statements = "INSERT INTO \"special_need\" (\"type\") VALUES ('ELEVATOR_REQUIRED')",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void rejectsFacilityNeedsNoLongerInTheAvailableCatalogue() {
        planWithSelection("[\"ELEVATOR_REQUIRED\"]").then().statusCode(400);
        assertThat(otpRequest.get()).isNull();
    }
}
