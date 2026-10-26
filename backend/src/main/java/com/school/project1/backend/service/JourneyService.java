package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.dto.otp.OtpPlanResponse;
import com.school.project1.backend.dto.RoutePlanRequestDto;
import com.school.project1.backend.model.facilities.PhysicalFacility;
import com.school.project1.backend.repository.dao.FacilityDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JourneyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JourneyService.class);

  private final OtpClient otpClient;
  private final FacilityDao facilityDao;

  public JourneyService(OtpClient otpClient, FacilityDao facilityDao) {
    this.otpClient = otpClient;
    this.facilityDao = facilityDao;
  }

  public JourneyResponse planRoute(RoutePlanRequestDto request) {
    if (request == null
      || request.originStationId() == null
      || request.originStationId().isBlank()
      || request.destinationStationId() == null
      || request.destinationStationId().isBlank()
      || request.originStationId().equals(request.destinationStationId())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Kies twee verschillende stations"
      );
    }

    OtpPlanResponse response;
    try {
      response = otpClient.planRoute(request);
    } catch (RestClientException exception) {
      throw new ResponseStatusException(
        HttpStatus.BAD_GATEWAY,
        "De routeplanner is niet bereikbaar",
        exception
      );
    }

    if (response == null || response.errors() != null && !response.errors().isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_GATEWAY,
        "De routeplanner gaf geen geldig antwoord"
      );
    }

    if (response.data() == null
      || response.data().planConnection() == null
      || response.data().planConnection().edges() == null
      || response.data().planConnection().edges().isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Geen reis gevonden tussen deze stations"
      );
    }

    OtpPlanResponse.Journey journey =
      response.data().planConnection().edges().getFirst().node();

    if (journey == null || journey.legs() == null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_GATEWAY,
        "De routeplanner gaf geen geldig antwoord"
      );
    }

    Map<String, List<PhysicalFacility>> facilitiesByStop = findFacilities(journey.legs());

    return new JourneyResponse(
      journey.start(),
      journey.end(),
      journey.legs().stream()
        .map(leg -> mapLeg(leg, facilitiesByStop))
        .toList()
    );
  }

  private Map<String, List<PhysicalFacility>> findFacilities(List<OtpPlanResponse.Leg> legs) {
    Set<String> stopIds = legs.stream()
      .flatMap(leg -> Stream.of(leg.from(), leg.to()))
      .map(JourneyService::gtfsIdOf)
      .filter(Objects::nonNull)
      .collect(Collectors.toSet());

    try {
      return facilityDao.findByStopIds(stopIds);
    } catch (DataAccessException exception) {
      LOGGER.warn("Could not load facilities, returning journey without facility data", exception);
      return Map.of();
    }
  }

  private JourneyResponse.LegResponse mapLeg(
    OtpPlanResponse.Leg leg,
    Map<String, List<PhysicalFacility>> facilitiesByStop
  ) {
    return new JourneyResponse.LegResponse(
      leg.mode(),
      leg.headsign(),
      leg.start() == null ? null : leg.start().scheduledTime(),
      leg.end() == null ? null : leg.end().scheduledTime(),
      leg.route() == null ? null : leg.route().shortName(),
      leg.trip() == null ? null : leg.trip().tripShortName(),
      leg.from().name(),
      leg.from().stop() == null ? null : leg.from().stop().platformCode(),
      leg.to().name(),
      leg.to().stop() == null ? null : leg.to().stop().platformCode(),
      facilitiesAt(leg.from(), facilitiesByStop),
      facilitiesAt(leg.to(), facilitiesByStop)
    );
  }

  private List<JourneyResponse.FacilityResponse> facilitiesAt(
    OtpPlanResponse.Place place,
    Map<String, List<PhysicalFacility>> facilitiesByStop
  ) {
    String stopId = gtfsIdOf(place);
    if (stopId == null || !facilitiesByStop.containsKey(stopId)) {
      return null;
    }

    return facilitiesByStop.get(stopId).stream()
      .map(facility -> new JourneyResponse.FacilityResponse(facility.type(), facility.operational()))
      .toList();
  }

  private static String gtfsIdOf(OtpPlanResponse.Place place) {
    if (place == null || place.stop() == null) {
      return null;
    }
    return place.stop().gtfsId();
  }
}
