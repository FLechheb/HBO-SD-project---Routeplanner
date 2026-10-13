package com.school.project1.backend.controllers;

import com.school.project1.backend.model.needs.NeedType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Arrays;

import static java.lang.IO.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.docker.compose.enabled=false"
)
class SpecialNeedControllerIntegrationTest {

    @Value("${test.base-uri}")
    private String baseUri;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = baseUri;
        RestAssured.port = port;
    }

    @AfterEach
    void resetRestAssured() {
        RestAssured.reset();
    }

    @Test
    void getNeeds() {

        RestAssured.get("/api/needs")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("type", containsInAnyOrder(
                Arrays.stream(NeedType.values()).map(Enum::name).toArray(String[]::new)
            ));
    }
}
