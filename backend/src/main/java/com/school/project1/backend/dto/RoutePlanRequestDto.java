package com.school.project1.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;

public final class RoutePlanRequestDto {

    private final String originStationId;
    private final String destinationStationId;
    private final Integer extraTransferMinutes;
    // Omitted selections keep the default; the setter rejects explicit JSON null.
    private List<String> selectedFacilities = List.of();

    @JsonCreator
    public RoutePlanRequestDto(
        @JsonProperty("originStationId") String originStationId,
        @JsonProperty("destinationStationId") String destinationStationId,
        @JsonProperty("extraTransferMinutes") Integer extraTransferMinutes
    ) {
        this.originStationId = originStationId;
        this.destinationStationId = destinationStationId;
        this.extraTransferMinutes = extraTransferMinutes;
    }

    @JsonProperty("originStationId")
    public String originStationId() {
        return originStationId;
    }

    @JsonProperty("destinationStationId")
    public String destinationStationId() {
        return destinationStationId;
    }

    @JsonProperty("extraTransferMinutes")
    public Integer extraTransferMinutes() {
        return extraTransferMinutes;
    }

    @JsonProperty("selectedFacilities")
    public List<String> selectedFacilities() {
        return selectedFacilities;
    }

    @JsonSetter(value = "selectedFacilities", nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    public void setSelectedFacilities(List<String> selectedFacilities) {
        this.selectedFacilities = selectedFacilities.stream().distinct().toList();
    }
}
