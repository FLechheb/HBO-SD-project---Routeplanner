package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.model.needs.FacilityNeed;
import com.school.project1.backend.model.needs.SpecialNeed;
import com.school.project1.backend.model.needs.TimingNeed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialNeedMapper {

    @Mapping(target = "category", source = "specialNeed")
    SpecialNeedDto toDto(SpecialNeed specialNeed);

    default SpecialNeedDto.Category toCategory(SpecialNeed specialNeed) {
        return switch (specialNeed) {
            case FacilityNeed ignored -> SpecialNeedDto.Category.FACILITY;
            case TimingNeed ignored -> SpecialNeedDto.Category.TIMING;
        };
    }
}
