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
    String from,
    String to
  ) {}
}