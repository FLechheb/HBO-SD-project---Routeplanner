package com.school.project1.backend.model;

public record RoutePlanRequest(
        String originStationId,
        String destinationStationId
) {
}