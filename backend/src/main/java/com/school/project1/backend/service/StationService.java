package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dto.otp.OtpStationsResponse;
import com.school.project1.backend.dto.StationResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StationService {

  private final OtpClient otpClient;

  public StationService(OtpClient otpClient) {
    this.otpClient = otpClient;
  }

  public List<StationResponseDto> searchStations(String name) {
    if (name == null || name.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vul een statiosnaam in");
    }

    OtpStationsResponse response = otpClient.searchStations(name.trim());

    if (response == null
      || response.errors() != null && !response.errors().isEmpty()
      || response.data() == null
      || response.data().stations() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Stations konden niet worden opgehaald");
    }

    return response.data().stations().stream()
      .map(station -> new StationResponseDto(station.gtfsId(), station.name()))
      .toList();
  }
}
