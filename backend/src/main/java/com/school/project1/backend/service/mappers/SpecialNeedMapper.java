package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.model.needs.SpecialNeed;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialNeedMapper {

    SpecialNeedDto toDto(SpecialNeed specialNeed);
}
