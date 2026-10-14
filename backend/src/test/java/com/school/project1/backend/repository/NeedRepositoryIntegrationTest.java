package com.school.project1.backend.repository;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.needs.NeedType;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.datasource.generate-unique-name=true",
    "spring.docker.compose.enabled=false"
})
class NeedRepositoryIntegrationTest {

    @Autowired
    private NeedRepository repository;

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void migrationsSeedEveryRequirementWithoutDisabilityPresetColumns() {
        // Arrange
        NeedType[] expectedTypes = NeedType.values();

        // Act
        var validation = flyway.validateWithResult();
        var needs = repository.findAll();
        var rows = jdbcTemplate.queryForList("SELECT * FROM \"special_need\"");

        // Assert
        assertThat(validation.validationSuccessful).isTrue();
        assertThat(needs).extracting(Need::type).containsExactlyInAnyOrder(expectedTypes);
        assertThat(rows)
            .allSatisfy(row -> assertThat(row).containsOnlyKeys("type"));
    }

    @Test
    void returnsNeedsOrderedByType() {
        // Arrange
        var expectedOrder = Arrays.stream(NeedType.values()).map(Enum::name).sorted().toList();

        // Act
        var needs = repository.getAll();
        var allNeeds = repository.findAll();

        // Assert
        assertThat(needs).extracting(need -> need.type().name()).containsExactlyElementsOf(expectedOrder);
        assertThat(needs).containsExactlyInAnyOrderElementsOf(allNeeds);
    }

    @Test
    void findsNeedByEnumIdentifier() {
        // Arrange
        NeedType type = NeedType.WHEELCHAIR_ACCESSIBLE;
        Need expectedNeed = new Need(type);

        // Act
        var need = repository.findById(type);

        // Assert
        assertThat(need).contains(expectedNeed);
    }
}
