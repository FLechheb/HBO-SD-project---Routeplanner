package com.school.project1.backend.dto;

import java.util.List;

public record OtpStationsResponse(Data data, List<GraphQlError> errors) {
  public record Data(List<Station> stations) {}

  public record Station(String gtfsId, String name) {}

  public record GraphQlError(String message) {}
}
