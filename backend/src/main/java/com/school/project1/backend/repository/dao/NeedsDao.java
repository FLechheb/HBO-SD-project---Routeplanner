package com.school.project1.backend.repository.dao;

import com.school.project1.backend.model.needs.MobilityNeed;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SensoryNeed;
import com.school.project1.backend.model.needs.SpecialNeed;
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

        return repo.findAll().stream().map(need -> switch (need.type()) {

            case NeedType.BLIND, NeedType.DEAF -> new SensoryNeed(need);
            case NeedType.WHEELCHAIR_BOUND, NeedType.ELDERLY, NeedType.PHYSICAL_IMPAIRED -> new MobilityNeed(need);

        }).toList();
    }
}
