package com.school.project1.backend.controllers;

import com.school.project1.backend.model.RoutePlanRequest;
import com.school.project1.backend.model.Stop;
import com.school.project1.backend.service.RouteService;
import com.school.project1.backend.service.StopService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController 
@RequestMapping("/api/routes")
public class RouteController {
  
  private final RouteService routeService;
  private final StopService stopService;

  public RouteController(RouteService routeService, StopService stopService) {
    this.routeService = routeService;
    this.stopService = stopService;
  }

  @GetMapping
  public List<Stop> getStops() {
    return stopService.getStops();
  }

  @PostMapping(value = "/plan", produces = MediaType.APPLICATION_JSON_VALUE)
  public String planRoute(@RequestBody RoutePlanRequest request) {
    return routeService.planRoute(request);
  }
}
