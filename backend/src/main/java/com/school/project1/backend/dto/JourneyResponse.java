package com.school.project1.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record JourneyResponse(
  OffsetDateTime departureTime,
  OffsetDateTime arrivalTime,
  List<LegResponse> legs
) {
  
  public record LegResponse(
    String mode,
    String headsign,
    OffsetDateTime departureTime,
    OffsetDateTime arrivalTime,
    String routeName,
    String tripNumber,
    String from,
    String departurePlatform,
    String to,
    String arrivalPlatform,
    List<FacilityResponse> departureFacilities,
    List<FacilityResponse> arrivalFacilities
  ) {}

  public record FacilityResponse(String type, boolean operational) {}
}