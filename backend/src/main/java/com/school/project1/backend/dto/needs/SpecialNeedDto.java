package com.school.project1.backend.dto.needs;

import com.school.project1.backend.model.needs.NeedType;

public record SpecialNeedDto(
  NeedType type,
  boolean extraTime,
  boolean seating,
  boolean levelFloor,
  boolean visualAid,
  boolean auditoryAid
){
}
