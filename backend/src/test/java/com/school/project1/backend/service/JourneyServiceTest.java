package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.dto.otp.OtpPlanResponse;
import com.school.project1.backend.dto.RoutePlanRequestDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JourneyServiceTest {

  private final OtpClient otpClient = mock(OtpClient.class);
  private final JourneyService journeyService =
    new JourneyService(otpClient, mock(SpecialNeedService.class));

  @ParameterizedTest
  @ValueSource(ints = {0, 5, 10, 15})
  void mapsOtpJourneyToRouteResponse(int extraTransferMinutes) {
    RoutePlanRequestDto request =
      new RoutePlanRequestDto(
        "NL:station:1",
        "NL:station:2",
        extraTransferMinutes);
    OffsetDateTime departure = OffsetDateTime.parse("2026-10-08T13:04:00+02:00");
    OffsetDateTime arrival = OffsetDateTime.parse("2026-10-08T13:31:00+02:00");

    OtpPlanResponse.Leg leg = new OtpPlanResponse.Leg(
      "RAIL",
      "Nijmegen",
      new OtpPlanResponse.LegTime(departure),
      new OtpPlanResponse.LegTime(arrival),
      new OtpPlanResponse.Route("Intercity"),
      new OtpPlanResponse.Trip("3051"),
      new OtpPlanResponse.Place("Amsterdam Centraal", new OtpPlanResponse.Stop("4b")),
      new OtpPlanResponse.Place("Utrecht Centraal", new OtpPlanResponse.Stop("19"))
    );

    OtpPlanResponse otpResponse = new OtpPlanResponse(
      new OtpPlanResponse.Data(
        new OtpPlanResponse.PlanConnection(
          List.of(new OtpPlanResponse.Edge(
            new OtpPlanResponse.Journey(departure, arrival, List.of(leg))
          ))
        )
      ),
      null
    );
    when(otpClient.planRoute(request)).thenReturn(otpResponse);

    JourneyResponse result = journeyService.planRoute(request);

    assertEquals(departure, result.departureTime());
    assertEquals(arrival, result.arrivalTime());
    assertEquals(1, result.legs().size());
    assertEquals("Intercity", result.legs().getFirst().routeName());
    assertEquals("4b", result.legs().getFirst().departurePlatform());
    assertEquals("19", result.legs().getFirst().arrivalPlatform());
  }

  @Test
  void rejectsSameOriginAndDestination() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:1", 0);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    verifyNoInteractions(otpClient);
  }

  @Test
  void returnsNotFoundWhenOtpFindsNoJourney() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2", 0);
    OtpPlanResponse response = new OtpPlanResponse(
      new OtpPlanResponse.Data(
        new OtpPlanResponse.PlanConnection(List.of())
      ),
      null
    );
    when(otpClient.planRoute(request)).thenReturn(response);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void returnsBadGatewayWhenOtpReportsAnError() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2", 0);
    OtpPlanResponse response = new OtpPlanResponse(
      null,
      List.of(new OtpPlanResponse.GraphQlError("Routeplanner niet beschikbaar"))
    );
    when(otpClient.planRoute(request)).thenReturn(response);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
  }

  @Test
  void keepsJourneyWhenPlatformCodesAreMissing() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2", 0);
    OffsetDateTime departure = OffsetDateTime.parse("2026-10-08T13:04:00+02:00");
    OffsetDateTime arrival = OffsetDateTime.parse("2026-10-08T13:31:00+02:00");

    OtpPlanResponse.Leg leg = new OtpPlanResponse.Leg(
      "RAIL",
      "Nijmegen",
      new OtpPlanResponse.LegTime(departure),
      new OtpPlanResponse.LegTime(arrival),
      new OtpPlanResponse.Route("Intercity"),
      new OtpPlanResponse.Trip("3051"),
      new OtpPlanResponse.Place("Amsterdam Centraal", null),
      new OtpPlanResponse.Place("Utrecht Centraal", null)
    );

    OtpPlanResponse response = new OtpPlanResponse(
      new OtpPlanResponse.Data(
        new OtpPlanResponse.PlanConnection(
          List.of(new OtpPlanResponse.Edge(
            new OtpPlanResponse.Journey(departure, arrival, List.of(leg))
          ))
        )
      ),
      null
    );
    when(otpClient.planRoute(request)).thenReturn(response);

    JourneyResponse result = journeyService.planRoute(request);

    assertEquals(1, result.legs().size());
    assertNull(result.legs().getFirst().departurePlatform());
    assertNull(result.legs().getFirst().arrivalPlatform());
  }

  @Test
  void returnsBadGatewayWhenOtpIsUnreachable() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2", 0);
    when(otpClient.planRoute(request))
      .thenThrow(new ResourceAccessException("Connection refused"));

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
  }

  @Test
  void rejectsMissingExtraTransferMinutes() {
    RoutePlanRequestDto request =
      new RoutePlanRequestDto("NL:station:1", "NL:station:2", null);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    verifyNoInteractions(otpClient);
  }

  @Test
  void rejectsUnsupportedExtraTransferMinutes() {
    RoutePlanRequestDto request =
      new RoutePlanRequestDto("NL:station:1", "NL:station:2", 7);

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    verifyNoInteractions(otpClient);
  }
}
