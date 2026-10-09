package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.otp.OtpStationsResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StationServiceTest {

  private final OtpClient otpClient = mock(OtpClient.class);
  private final StationService stationService = new StationService(otpClient);

  @Test
  void mapsOtpStationsToPublicResponse() {
    when(otpClient.searchStations("Utrecht")).thenReturn(new OtpStationsResponse(
      new OtpStationsResponse.Data(List.of(
        new OtpStationsResponse.Station("NL:station:1", "Utrecht Centraal")
      )),
      null
    ));

    var stations = stationService.searchStations(" Utrecht ");

    assertEquals(1, stations.size());
    assertEquals("NL:station:1", stations.getFirst().gtfsId());
    assertEquals("Utrecht Centraal", stations.getFirst().name());
  }
}