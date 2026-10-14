package com.school.project1.backend.repository.dao;

import com.school.project1.backend.model.needs.SpecialNeed;
import com.school.project1.backend.model.needs.SpecialNeedFactory;
import com.school.project1.backend.repository.NeedRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NeedsDao {

    private final NeedRepository repo;

    public NeedsDao(NeedRepository repo) {
        this.repo = repo;
    }

    public List<SpecialNeed> getAll() {

        return repo.getAll().stream()
            .map(need -> SpecialNeedFactory.create(need.type()))
            .toList();
    }
}
