package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.dto.otp.OtpPlanResponse;
import com.school.project1.backend.dto.RoutePlanRequestDto;
import com.school.project1.backend.model.facilities.PhysicalFacility;
import com.school.project1.backend.repository.dao.FacilityDao;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.ResourceAccessException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JourneyServiceTest {

  private static final OtpPlanResponse.Place UTRECHT_PLATFORM_7 =
    new OtpPlanResponse.Place("Utrecht Centraal", new OtpPlanResponse.Stop("NL:2994013", "7"));

  private static final OtpPlanResponse.Place AMSTERDAM_PLATFORM_7A =
    new OtpPlanResponse.Place("Amsterdam Centraal", new OtpPlanResponse.Stop("NL:2992194", "7a"));

  private final OtpClient otpClient = mock(OtpClient.class);
  private final FacilityDao facilityDao = mock(FacilityDao.class);
  private final JourneyService journeyService = new JourneyService(otpClient, facilityDao);

  @Test
  void mapsOtpJourneyToRouteResponse() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
    OffsetDateTime departure = OffsetDateTime.parse("2026-10-08T13:04:00+02:00");
    OffsetDateTime arrival = OffsetDateTime.parse("2026-10-08T13:31:00+02:00");

    OtpPlanResponse.Leg leg = new OtpPlanResponse.Leg(
      "RAIL",
      "Nijmegen",
      new OtpPlanResponse.LegTime(departure),
      new OtpPlanResponse.LegTime(arrival),
      new OtpPlanResponse.Route("Intercity"),
      new OtpPlanResponse.Trip("3051"),
      new OtpPlanResponse.Place("Amsterdam Centraal", new OtpPlanResponse.Stop("NL:stop:asd-4b", "4b")),
      new OtpPlanResponse.Place("Utrecht Centraal", new OtpPlanResponse.Stop("NL:stop:ut-19", "19"))
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
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:1");

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    verifyNoInteractions(otpClient);
  }

  @Test
  void returnsNotFoundWhenOtpFindsNoJourney() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
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
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
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
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
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
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
    when(otpClient.planRoute(request))
      .thenThrow(new ResourceAccessException("Connection refused"));

    ResponseStatusException exception = assertThrows(
      ResponseStatusException.class,
      () -> journeyService.planRoute(request)
    );

    assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
  }

  @Test
  void addsFacilitiesOfDepartureAndArrivalStop() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
    when(otpClient.planRoute(request)).thenReturn(journeyBetween(UTRECHT_PLATFORM_7, AMSTERDAM_PLATFORM_7A));
    when(facilityDao.findByStopIds(Set.of("NL:2994013", "NL:2992194"))).thenReturn(Map.of(
      "NL:2994013", List.of(new PhysicalFacility("ELEVATOR", true), new PhysicalFacility("ELEVATOR", false)),
      "NL:2992194", List.of()
    ));

    JourneyResponse.LegResponse leg = journeyService.planRoute(request).legs().getFirst();

    assertEquals(
      List.of(
        new JourneyResponse.FacilityResponse("ELEVATOR", true),
        new JourneyResponse.FacilityResponse("ELEVATOR", false)
      ),
      leg.departureFacilities()
    );
    assertEquals(List.of(), leg.arrivalFacilities());
  }

  @Test
  void returnsNoFacilityDataForStopsThatAreNotInTheDatabase() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
    when(otpClient.planRoute(request)).thenReturn(journeyBetween(UTRECHT_PLATFORM_7, AMSTERDAM_PLATFORM_7A));
    when(facilityDao.findByStopIds(Set.of("NL:2994013", "NL:2992194"))).thenReturn(Map.of());

    JourneyResponse.LegResponse leg = journeyService.planRoute(request).legs().getFirst();

    assertNull(leg.departureFacilities());
    assertNull(leg.arrivalFacilities());
  }

  @Test
  void keepsJourneyWhenFacilitiesCannotBeLoaded() {
    RoutePlanRequestDto request = new RoutePlanRequestDto("NL:station:1", "NL:station:2");
    when(otpClient.planRoute(request)).thenReturn(journeyBetween(UTRECHT_PLATFORM_7, AMSTERDAM_PLATFORM_7A));
    when(facilityDao.findByStopIds(Set.of("NL:2994013", "NL:2992194")))
      .thenThrow(new DataAccessResourceFailureException("Database unavailable"));

    JourneyResponse result = journeyService.planRoute(request);

    assertEquals(1, result.legs().size());
    assertNull(result.legs().getFirst().departureFacilities());
    assertNull(result.legs().getFirst().arrivalFacilities());
  }

  private static OtpPlanResponse journeyBetween(OtpPlanResponse.Place from, OtpPlanResponse.Place to) {
    OffsetDateTime departure = OffsetDateTime.parse("2026-10-08T13:04:00+02:00");
    OffsetDateTime arrival = OffsetDateTime.parse("2026-10-08T13:31:00+02:00");
    OtpPlanResponse.Leg leg = new OtpPlanResponse.Leg(
      "RAIL",
      "Amsterdam Centraal",
      new OtpPlanResponse.LegTime(departure),
      new OtpPlanResponse.LegTime(arrival),
      new OtpPlanResponse.Route("Intercity"),
      new OtpPlanResponse.Trip("3051"),
      from,
      to
    );

    return new OtpPlanResponse(
      new OtpPlanResponse.Data(
        new OtpPlanResponse.PlanConnection(
          List.of(new OtpPlanResponse.Edge(
            new OtpPlanResponse.Journey(departure, arrival, List.of(leg))
          ))
        )
      ),
      null
    );
  }
}
