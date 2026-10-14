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
        assertThat(flyway.validateWithResult().validationSuccessful).isTrue();

        var needs = repository.findAll();
        assertThat(needs).extracting(Need::type).containsExactlyInAnyOrder(NeedType.values());
        assertThat(jdbcTemplate.queryForList("SELECT * FROM \"special_need\""))
            .allSatisfy(row -> assertThat(row).containsOnlyKeys("type"));
    }

    @Test
    void returnsNeedsOrderedByType() {
        var needs = repository.getAll();
        assertThat(needs).extracting(need -> need.type().name()).containsExactlyElementsOf(
            Arrays.stream(NeedType.values()).map(Enum::name).sorted().toList()
        );
        assertThat(needs).containsExactlyInAnyOrderElementsOf(repository.findAll());
    }

    @Test
    void findsNeedByEnumIdentifier() {
        assertThat(repository.findById(NeedType.WHEELCHAIR_ACCESSIBLE))
            .contains(new Need(NeedType.WHEELCHAIR_ACCESSIBLE));
    }
}
