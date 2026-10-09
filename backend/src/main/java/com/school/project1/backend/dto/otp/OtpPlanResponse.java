package com.school.project1.backend.dto.otp;

import java.time.OffsetDateTime;
import java.util.List;

public record OtpPlanResponse(
  Data data,
  List<GraphQlError> errors
) {
  public record Data(PlanConnection planConnection) {}

  public record PlanConnection(List<Edge> edges) {}

  public record Edge(Journey node) {}

  public record Journey(
    OffsetDateTime start,
    OffsetDateTime end,
    List<Leg> legs
  ) {}

  public record Leg(
  String mode,
  String headsign,
  LegTime start,
  LegTime end,
  Route route,
  Trip trip,
  Place from,
  Place to
  ) {}

  public record LegTime(OffsetDateTime scheduledTime) {}

  public record Route(String shortName) {}

  public record Trip(String tripShortName) {}

  public record Place(String name, Stop stop) {}

  public record Stop(String platformCode) {}

  public record GraphQlError(String message) {}
}
