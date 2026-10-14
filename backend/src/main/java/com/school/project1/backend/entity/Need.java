package com.school.project1.backend.entity;

import com.school.project1.backend.model.needs.NeedType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("special_need")
public record Need(
    @Id @Column("type") NeedType type
) {
}
