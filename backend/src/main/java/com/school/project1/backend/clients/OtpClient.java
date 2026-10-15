package com.school.project1.backend.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.school.project1.backend.dto.otp.OtpPlanResponse;
import com.school.project1.backend.dto.otp.OtpStationsResponse;
import com.school.project1.backend.dto.RoutePlanRequestDto;

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
                      from { name stop { gtfsId platformCode } }
                      to { name stop { gtfsId platformCode } }
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

    private record GraphQlRequest(String query, Map<String, String> variables) {}

    public OtpClient(RestClient.Builder builder, @Value("${otp.base-url}") String baseUrl) {
      HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(5))
        .build();

      JdkClientHttpRequestFactory requestFactory =
        new JdkClientHttpRequestFactory(httpClient);
      requestFactory.setReadTimeout(Duration.ofSeconds(60));

      this.restClient = builder
        .baseUrl(baseUrl)
        .requestFactory(requestFactory)
        .build();
    }

    public OtpPlanResponse planRoute(RoutePlanRequestDto request) {
      return postQuery(
        PLAN_QUERY,
        Map.of(
          "originStationId", request.originStationId(),
          "destinationStationId", request.destinationStationId()
        ),
        OtpPlanResponse.class
      );
    }

    public OtpStationsResponse searchStations(String name) {
      return postQuery(
        STATIONS_QUERY,
        Map.of("name", name),
        OtpStationsResponse.class
      );
    }

    private <T> T postQuery(String query, Map<String, String> variables, Class<T> responseType) {
      return restClient.post()
        .uri("/otp/gtfs/v1")
        .contentType(MediaType.APPLICATION_JSON)
        .body(new GraphQlRequest(query, variables))
        .retrieve()
        .body(responseType);
    }
}
