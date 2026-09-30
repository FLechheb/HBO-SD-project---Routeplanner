package com.school.project1.backend.controllers;

import com.school.project1.backend.model.Stop;
import com.school.project1.backend.service.StopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final StopService stopService;

    public RouteController(StopService stopService) {
        this.stopService = stopService;
    }

    @GetMapping
    public List<Stop> getStops() {
        return stopService.getStops();
    }
}
