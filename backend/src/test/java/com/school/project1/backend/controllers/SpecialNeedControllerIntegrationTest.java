package com.school.project1.backend.controllers;

import com.school.project1.backend.model.needs.NeedType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        // Arrange
        List<NeedType> types = Arrays.stream(NeedType.values())
            .sorted(Comparator.comparing(Enum::name))
            .toList();
        List<Map<String, Object>> expectedCatalogue =
            types.stream().map(type -> Map.<String, Object>of(
                "type", type.name(),
                "description", type.getDescription(),
                "category", type == NeedType.EXTRA_TRANSFER_TIME ? "TIMING" : "FACILITY"
            )).toList();

        // Act
        Response response = RestAssured.get("/api/needs");

        // Assert
        response.then().statusCode(200).contentType("application/json");
        List<Map<String, Object>> catalogue = response.jsonPath().getList("$");
        assertThat(catalogue).containsExactlyElementsOf(expectedCatalogue);
    }
}
