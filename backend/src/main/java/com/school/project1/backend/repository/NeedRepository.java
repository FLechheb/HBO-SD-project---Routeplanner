package com.school.project1.backend.repository;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.needs.NeedType;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedRepository extends ListCrudRepository<Need, NeedType> {

    @Query("""
        SELECT "type", "extra_time", "seating", "level_floor", "visual_aid", "auditory_aid"
        FROM "special_need"
        ORDER BY "type"
        """)
    List<Need> getAll();
}