package com.school.project1.backend.dto;

public record RoutePlanRequestDto(
        String originStationId,
        String destinationStationId,
        Integer extraTransferMinutes
) {
}