package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.needs.MobilityNeed;
import com.school.project1.backend.model.needs.SensoryNeed;
import com.school.project1.backend.model.needs.SpecialNeed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialNeedMapper {

    @Mapping(target = "type", source = "specialNeed.need.type")
    @Mapping(target = "extraTime", source = "specialNeed.need.extraTime")
    @Mapping(target = "seating", source = "specialNeed.need.seating")
    @Mapping(target = "levelFloor", source = "specialNeed.need.levelFloor")
    @Mapping(target = "visualAid", source = "specialNeed.need.visualAid")
    @Mapping(target = "auditoryAid", source = "specialNeed.need.auditoryAid")
    SpecialNeedDto toDto(SpecialNeed specialNeed);

    default SpecialNeed fromDto(SpecialNeedDto dto) {
        Need need = toNeed(dto);

        return switch (need.type()) {
            case WHEELCHAIR_BOUND, PHYSICAL_IMPAIRED, ELDERLY -> new MobilityNeed(need);
            case DEAF, BLIND -> new SensoryNeed(need);
        };
    }

    Need toNeed(SpecialNeedDto dto);
}

