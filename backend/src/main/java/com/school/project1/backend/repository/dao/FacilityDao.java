package com.school.project1.backend.repository.dao;

import com.school.project1.backend.model.facilities.PhysicalFacility;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FacilityDao {

    private static final String FIND_BY_STOP_IDS = """
        SELECT s."gtfs_id" AS "stop_id", ft."name" AS "type", f."operational"
        FROM "stop" s
        LEFT JOIN "stop_facility" sf ON sf."stop_gtfs_id" = s."gtfs_id"
        LEFT JOIN "facility" f ON f."id" = sf."facility_id"
        LEFT JOIN "facility_type" ft ON ft."id" = f."facility_type_id"
        WHERE s."gtfs_id" IN (:stopIds)
        ORDER BY s."gtfs_id", ft."name"
        """;

    private final JdbcClient jdbcClient;

    public FacilityDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Map<String, List<PhysicalFacility>> findByStopIds(Collection<String> stopIds) {
        if (stopIds.isEmpty()) {
            return Map.of();
        }

        return jdbcClient.sql(FIND_BY_STOP_IDS)
            .param("stopIds", stopIds)
            .query(FacilityRow.class)
            .list()
            .stream()
            .collect(Collectors.groupingBy(
                FacilityRow::stopId,
                Collectors.flatMapping(row -> row.toFacility().stream(), Collectors.toList())
            ));
    }

    private record FacilityRow(String stopId, String type, Boolean operational) {

        Optional<PhysicalFacility> toFacility() {
            if (type == null) {
                return Optional.empty();
            }
            return Optional.of(new PhysicalFacility(type, operational));
        }
    }
}
