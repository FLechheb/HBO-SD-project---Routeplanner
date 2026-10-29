package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.dto.RoutePlanRequestDto;
import com.school.project1.backend.dto.otp.OtpPlanResponse;
import com.school.project1.backend.model.needs.FacilityNeed;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JourneyService {

  private static final Set<Integer> ALLOWED_EXTRA_TRANSFER_MINUTES =
    Set.of(0, 5, 10, 15);

  private final OtpClient otpClient;
  private final SpecialNeedService specialNeedService;

  public JourneyService(OtpClient otpClient, SpecialNeedService specialNeedService) {
    this.otpClient = otpClient;
    this.specialNeedService = specialNeedService;
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

    if (request.extraTransferMinutes() == null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Extra overstaptijd is verplicht"
      );
    }

    if (!ALLOWED_EXTRA_TRANSFER_MINUTES.contains(request.extraTransferMinutes())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Ongeldige extra overstaptijd"
      );
    }

    if (!request.selectedFacilities().isEmpty()) {
      Set<String> availableFacilities = specialNeedService.getAllNeeds().stream()
        .filter(FacilityNeed.class::isInstance)
        .map(need -> need.getType().name())
        .collect(Collectors.toSet());
      if (!availableFacilities.containsAll(request.selectedFacilities())) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Ongeldige voorzieningen. Controleer Mijn profiel"
        );
      }
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
        .map(this::mapLeg)
        .toList()
    );
  }

  private JourneyResponse.LegResponse mapLeg(OtpPlanResponse.Leg leg) {
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
      leg.to().stop() == null ? null : leg.to().stop().platformCode()
    );
  }
}
