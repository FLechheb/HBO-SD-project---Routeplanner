package com.school.project1.backend.repository.dao;

import com.school.project1.backend.entity.Need;
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

        return repo.findAll().stream().map(n -> switch(n.type()){

            case NeedType.BLIND -> new SensoryNeed(n);
            case NeedType.DEAF -> new SensoryNeed(n);

            case NeedType.WHEELCHAIR_BOUND -> new MobilityNeed(n);
            case NeedType.PHYSICAL_IMPAIRED -> new MobilityNeed(n);
            case NeedType.ELDERLY -> new MobilityNeed(n);

        }).toList();
    }
}
