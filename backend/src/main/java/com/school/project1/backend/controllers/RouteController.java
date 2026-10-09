package com.school.project1.backend.controllers;

import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.dto.RoutePlanRequestDto;
import com.school.project1.backend.dto.StationResponseDto;
import com.school.project1.backend.service.JourneyService;
import com.school.project1.backend.service.StationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

  private final JourneyService journeyService;
  private final StationService stationService;

  public RouteController(JourneyService journeyService, StationService stationService) {
    this.journeyService = journeyService;
    this.stationService = stationService;
  }

  @PostMapping("/plan")
  public JourneyResponse planRoute(@RequestBody RoutePlanRequestDto request) {
    return journeyService.planRoute(request);
  }

  @GetMapping("/stations")
  public List<StationResponseDto> searchStations(
    @RequestParam String name
  ) {
    return stationService.searchStations(name);
  }
}
