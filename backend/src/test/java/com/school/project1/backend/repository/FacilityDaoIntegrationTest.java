package com.school.project1.backend.repository;

import com.school.project1.backend.model.facilities.PhysicalFacility;
import com.school.project1.backend.repository.dao.FacilityDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.datasource.generate-unique-name=true",
    "spring.docker.compose.enabled=false"
})
@Transactional
class FacilityDaoIntegrationTest {

    @Autowired
    private FacilityDao facilityDao;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void returnsSharedFacilityForEveryStopItIsLinkedTo() {
        // Arrange
        insertStop("TEST:5a");
        insertStop("TEST:5b");
        long elevatorId = insertFacility("ELEVATOR", false);
        linkFacility("TEST:5a", elevatorId);
        linkFacility("TEST:5b", elevatorId);
        PhysicalFacility brokenElevator = new PhysicalFacility("ELEVATOR", false);

        // Act
        var facilitiesByStop = facilityDao.findByStopIds(Set.of("TEST:5a", "TEST:5b"));

        // Assert
        assertThat(facilitiesByStop)
            .containsEntry("TEST:5a", List.of(brokenElevator))
            .containsEntry("TEST:5b", List.of(brokenElevator));
    }

    @Test
    void returnsEmptyListForKnownStopWithoutFacilities() {
        // Arrange
        insertStop("TEST:empty");

        // Act
        var facilitiesByStop = facilityDao.findByStopIds(Set.of("TEST:empty"));

        // Assert
        assertThat(facilitiesByStop).containsEntry("TEST:empty", List.of());
    }

    @Test
    void leavesOutStopsThatAreNotInTheDatabase() {
        // Arrange
        String unknownStopId = "TEST:unknown";

        // Act
        var facilitiesByStop = facilityDao.findByStopIds(Set.of(unknownStopId));

        // Assert
        assertThat(facilitiesByStop).doesNotContainKey(unknownStopId);
    }

    @Test
    void returnsEmptyMapWhenNoStopIdsAreGiven() {
        // Arrange
        Set<String> noStopIds = Set.of();

        // Act
        var facilitiesByStop = facilityDao.findByStopIds(noStopIds);

        // Assert
        assertThat(facilitiesByStop).isEmpty();
    }

    @Test
    void mockDataGivesUtrechtPlatform7TwoElevatorsOfWhichOneIsOutOfOrder() {
        // Arrange
        String utrechtPlatform7 = "NL:2994013";

        // Act
        var facilities = facilityDao.findByStopIds(Set.of(utrechtPlatform7)).get(utrechtPlatform7);

        // Assert
        assertThat(facilities).containsExactlyInAnyOrder(
            new PhysicalFacility("ELEVATOR", true),
            new PhysicalFacility("ELEVATOR", false),
            new PhysicalFacility("ESCALATOR", true),
            new PhysicalFacility("STAIRS", true)
        );
    }

    private void insertStop(String gtfsId) {
        jdbcClient.sql("""
                INSERT INTO "stop" ("gtfs_id", "name", "platform_code") VALUES (:gtfsId, 'Test station', NULL)
                """)
            .param("gtfsId", gtfsId)
            .update();
    }

    private long insertFacility(String type, boolean operational) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO "facility" ("facility_type_id", "operational")
                SELECT "id", :operational FROM "facility_type" WHERE "name" = :type
                """)
            .param("operational", operational)
            .param("type", type)
            .update(keyHolder, "id");
        return keyHolder.getKeyAs(Integer.class);
    }

    private void linkFacility(String gtfsId, long facilityId) {
        jdbcClient.sql("""
                INSERT INTO "stop_facility" ("stop_gtfs_id", "facility_id") VALUES (:gtfsId, :facilityId)
                """)
            .param("gtfsId", gtfsId)
            .param("facilityId", facilityId)
            .update();
    }
}
