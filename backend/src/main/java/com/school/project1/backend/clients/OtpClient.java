package com.school.project1.backend.clients;

import java.net.http.HttpClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import com.school.project1.backend.model.RoutePlanRequest;

@Service
public class OtpClient {
    private final RestClient restClient = RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory(
                    HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()))
            .build();

    public String planRoute(RoutePlanRequest request) {
        String query = String.format("""
                query TestPlan {
                  planConnection(
                    origin: { location: { coordinate: { latitude: %s, longitude: %s } } }
                    destination: { location: { coordinate: { latitude: %s, longitude: %s } } }
                  ) {
                    edges { node { start end legs { mode duration from { name } to { name } } } }
                  }
                }
                """,
                request.originLat(), request.originLon(),
                request.destinationLat(), request.destinationLon());

        String escapedQuery = query.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        String jsonBody = "{\"query\": \"" + escapedQuery + "\"}";

        System.out.println("OTP request body: " + jsonBody);

        return restClient.post()
                .uri("http://localhost:8080/otp/gtfs/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonBody)
                .retrieve()
                .body(String.class);
    }
}
