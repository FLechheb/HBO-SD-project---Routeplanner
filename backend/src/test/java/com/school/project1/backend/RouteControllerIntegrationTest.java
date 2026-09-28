package com.school.project1.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RouteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsOrderedStopsFromTheStandardRoute() throws Exception {
        mockMvc.perform(get("/api/routes"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(10))
            .andExpect(jsonPath("$[0].stopNumber").value(1))
            .andExpect(jsonPath("$[0].stopName").value("Utrecht Centraal"))
            .andExpect(jsonPath("$[0].arrivalTime").value("10:00"))
            .andExpect(jsonPath("$[0].facility").value("lift"))
            .andExpect(jsonPath("$[9].stopNumber").value(10))
            .andExpect(jsonPath("$[9].stopName").value("Amsterdam Centraal"));
    }
}