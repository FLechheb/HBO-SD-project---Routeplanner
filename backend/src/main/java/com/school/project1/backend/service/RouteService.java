package com.school.project1.backend.service;

import com.school.project1.backend.dto.RouteStopResponse;
import com.school.project1.backend.entity.StopEntity;
import com.school.project1.backend.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final StopRepository stopRepository;

    public RouteService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<RouteStopResponse> getStops() {
        return stopRepository.findAllOrderedByStopNumber()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    private RouteStopResponse mapToResponse(StopEntity stop) {
        return new RouteStopResponse(
            stop.stopNumber(),
            stop.stopName(),
            stop.arrivalTime(),
            stop.departureTime(),
            stop.facility()
        );
    }
}