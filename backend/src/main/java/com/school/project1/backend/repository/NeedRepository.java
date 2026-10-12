package com.school.project1.backend.repository;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.entity.StopEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedRepository extends ListCrudRepository<Need, Long> {

    @Query("""
        SELECT id
        FROM need
        ORDER BY id
        """)
    List<StopEntity> getAll();
}