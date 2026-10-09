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
class StopControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsStops() throws Exception {
        mockMvc.perform(get("/api/stops"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(10))
            .andExpect(jsonPath("$[0].stopCode").value("UT"))
            .andExpect(jsonPath("$[0].name").value("Utrecht Centraal"))
            .andExpect(jsonPath("$[9].stopCode").value("ZL"))
            .andExpect(jsonPath("$[9].name").value("Zwolle"));
    }
}
