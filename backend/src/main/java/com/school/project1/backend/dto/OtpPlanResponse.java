package com.school.project1.backend.dto;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;

import javax.xml.crypto.Data;
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
    Place from,
    Place to
  ) {}

  public record Place(String name) {}

  public record GraphQlError(String message) {}
}
