package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.dto.OtpPlanResponse;
import com.school.project1.backend.model.RoutePlanRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JourneyService {

  private final OtpClient otpClient;

  public JourneyService(OtpClient otpClient) {
    this.otpClient = otpClient;
  }

  public JourneyResponse planRoute(RoutePlanRequest request) {
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

    return new JourneyResponse(
      journey.start(),
      journey.end(),
      journey.legs().stream()
        .map(leg -> new JourneyResponse.LegResponse(
          leg.mode(),
          leg.headsign(),
          leg.start() == null ? null : leg.start().scheduledTime(),
          leg.end() == null ? null : leg.end().scheduledTime(),
          leg.route() == null ? null : leg.route().shortName(),
          leg.trip() == null ? null : leg.trip().tripShortName(),
          leg.from().name(),
          leg.from().stop() == null ? null : leg.from().stop().platformCode(),
          leg.to().name(),
          leg.to().stop() == null ? null : leg.to().stop().platformCode()
        ))
        .toList()
    );
  }
}