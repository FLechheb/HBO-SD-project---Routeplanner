package com.school.project1.backend;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.repository.NeedRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void migrationsSeedEveryNeedTypeWithProvisionalDefaults() {
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("6");
        assertThat(flyway.validateWithResult().validationSuccessful).isTrue();

        var needs = repository.findAll();
        assertThat(needs).extracting(Need::type).containsExactlyInAnyOrder(NeedType.values());
        assertThat(needs).containsExactlyInAnyOrder(
            new Need(NeedType.DEAF, false, false, false, true, false),
            new Need(NeedType.BLIND, true, false, false, false, true),
            new Need(NeedType.WHEELCHAIR_BOUND, true, false, true, false, false),
            new Need(NeedType.ELDERLY, true, true, true, false, false),
            new Need(NeedType.PHYSICAL_IMPAIRED, true, true, true, false, false)
        );
    }

    @Test
    void returnsNeedsOrderedByType() {
        var needs = repository.getAll();
        assertThat(needs).extracting(Need::type).containsExactly(
            NeedType.BLIND, NeedType.DEAF, NeedType.ELDERLY,
            NeedType.PHYSICAL_IMPAIRED, NeedType.WHEELCHAIR_BOUND
        );
        assertThat(needs).containsExactlyInAnyOrderElementsOf(repository.findAll());
    }

    @Test
    void findsNeedByEnumIdentifier() {
        assertThat(repository.findById(NeedType.BLIND))
            .contains(new Need(NeedType.BLIND, true, false, false, false, true));
    }
}
