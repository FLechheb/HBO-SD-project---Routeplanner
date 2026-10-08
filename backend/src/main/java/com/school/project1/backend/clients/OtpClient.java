package com.school.project1.backend.clients;

import com.school.project1.backend.model.RoutePlanRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.school.project1.backend.dto.OtpPlanResponse;
import com.school.project1.backend.dto.OtpStationsResponse;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

@Component
public class OtpClient {

    private static final String PLAN_QUERY = """
        query PlanRoute($originStationId: String!, $destinationStationId: String!) {
              planConnection(
                origin: {
                  location: {
                    stopLocation: { stopLocationId: $originStationId }
                  }
                }
                destination: {
                  location: {
                    stopLocation: { stopLocationId: $destinationStationId }
                  }
                }
                first: 1
              ) {
                edges {
                  node {
                    start
                    end
                    legs {
                      mode
                      headsign
                      start { scheduledTime }
                      end { scheduledTime }
                      route { shortName }
                      trip { tripShortName }
                      from { name stop { platformCode } }
                      to { name stop { platformCode } }
                    }
                  }
                }
              }
            }
        """;

    private static final String STATIONS_QUERY = """
      query SearchStations($name: String!) {
        stations(name: $name) {
          gtfsId
          name
        }
      }
      """;

    private final RestClient restClient;

    public OtpClient(RestClient.Builder builder) {
      HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(5))
        .build();

      JdkClientHttpRequestFactory requestFactory =
        new JdkClientHttpRequestFactory(httpClient);
      requestFactory.setReadTimeout(Duration.ofSeconds(60));

      this.restClient = builder
        .baseUrl("http://localhost:8085")
        .requestFactory(requestFactory)
        .build();
    }

    public OtpPlanResponse planRoute(RoutePlanRequest request) {
      Map<String, Object> body = Map.of(
        "query", PLAN_QUERY,
        "variables", Map.of(
          "originStationId", request.originStationId(),
          "destinationStationId", request.destinationStationId()
        )
      );

      return restClient.post()
        .uri("/otp/gtfs/v1")
        .contentType(MediaType.APPLICATION_JSON)
        .body(body)
        .retrieve()
        .body(OtpPlanResponse.class);
    }

    public OtpStationsResponse searchStations(String name) {
      return restClient.post()
        .uri("/otp/gtfs/v1")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Map.of(
          "query", STATIONS_QUERY,
          "variables", Map.of("name", name)
        ))
        .retrieve()
        .body(OtpStationsResponse.class);
    }
}



//@Service
//public class OtpClient {
//    private final RestClient restClient = RestClient.builder()
//            .requestFactory(new JdkClientHttpRequestFactory(
//                    HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()))
//            .build();
//
//    public String planRoute(RoutePlanRequest request) {
//        String query = String.format("""
//                query TestPlan {
//                  planConnection(
//                    origin: { location: { coordinate: { latitude: %s, longitude: %s } } }
//                    destination: { location: { coordinate: { latitude: %s, longitude: %s } } }
//                  ) {
//                    edges { node { start end legs { mode duration from { name } to { name } } } }
//                  }
//                }
//                """,
//                request.originLat(), request.originLon(),
//                request.destinationLat(), request.destinationLon());
//
//        String escapedQuery = query.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
//        String jsonBody = "{\"query\": \"" + escapedQuery + "\"}";
//
//        System.out.println("OTP request body: " + jsonBody);
//
//        return restClient.post()
//                .uri("http://localhost:8085/otp/gtfs/v1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(jsonBody)
//                .retrieve()
//                .body(String.class);
//    }
//}