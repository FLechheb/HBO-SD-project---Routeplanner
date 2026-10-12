package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SpecialNeed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SpecialNeedMapper {

    @Mapping(target = "type", source = "need.type", qualifiedByName = "description")
    SpecialNeedDto toDto(SpecialNeed specialNeed);

    @Named("description")
    default String description(NeedType type){
        return type.getDescription();
    }
}

