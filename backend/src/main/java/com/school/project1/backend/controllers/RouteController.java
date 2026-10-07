package com.school.project1.backend.controllers;

import com.school.project1.backend.dto.JourneyResponse;
import com.school.project1.backend.model.RoutePlanRequest;
import com.school.project1.backend.model.Stop;
import com.school.project1.backend.service.JourneyService;
import com.school.project1.backend.service.StopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.school.project1.backend.dto.OtpStationsResponse;
import com.school.project1.backend.service.StationService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

  private final StopService stopService;
  private final JourneyService journeyService;
  private final StationService stationService;

  public RouteController(StopService stopService, JourneyService journeyService, StationService stationService) {
    this.stopService = stopService;
    this.journeyService = journeyService;
    this.stationService = stationService;
  }

  @GetMapping
  public List<Stop> getStops() {
    return stopService.getStops();
  }

  @PostMapping("/plan")
  public JourneyResponse planRoute(@RequestBody RoutePlanRequest request) {
    return journeyService.planRoute(request);
  }

  @GetMapping("/stations")
  public List<OtpStationsResponse.Station> searchStations(
    @RequestParam String name
  ) {
    return stationService.searchStations(name);
  }
}
