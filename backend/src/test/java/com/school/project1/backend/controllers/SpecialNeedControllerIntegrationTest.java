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

        List<Map<String, Object>> catalogue = RestAssured.get("/api/needs")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .extract().jsonPath().getList("$");

        List<NeedType> types = Arrays.stream(NeedType.values())
            .sorted(Comparator.comparing(Enum::name))
            .toList();
        assertThat(catalogue).containsExactlyElementsOf(
            types.stream().map(type -> Map.<String, Object>of(
                "type", type.name(),
                "description", type.getDescription()
            )).toList()
        );
    }
}
