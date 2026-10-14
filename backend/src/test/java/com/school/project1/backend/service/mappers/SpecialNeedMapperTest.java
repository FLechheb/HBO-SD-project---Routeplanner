package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SpecialNeed;
import com.school.project1.backend.model.needs.SpecialNeedFactory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpecialNeedMapperTest {

    private final SpecialNeedMapper mapper = Mappers.getMapper(SpecialNeedMapper.class);

    @Test
    void mapsEveryPolymorphicNeedToItsCodeAndCanonicalDescription() {
        for (NeedType type : NeedType.values()) {
            // Arrange
            SpecialNeed need = SpecialNeedFactory.create(type);
            String expectedDescription = type.getDescription();

            // Act
            SpecialNeedDto dto = mapper.toDto(need);

            // Assert
            assertEquals(type, dto.type());
            assertEquals(expectedDescription, dto.description());
        }
    }
}
