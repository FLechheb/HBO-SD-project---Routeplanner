package com.school.project1.backend.entity;

import com.school.project1.backend.model.needs.NeedType;

public record Need(NeedType type, Boolean extraTime, Boolean seating, Boolean levelFloor, Boolean visualAid, Boolean auditoryAid) {
}
