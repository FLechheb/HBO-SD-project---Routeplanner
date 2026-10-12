package com.school.project1.backend.service;

import com.school.project1.backend.repository.dao.NeedsDao;
import com.school.project1.backend.model.needs.SpecialNeed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialNeedService {

    private final NeedsDao needsDao;

    public SpecialNeedService(NeedsDao needsDao) {
        this.needsDao = needsDao;
    }

    public List<SpecialNeed> getAllNeeds() {
        return needsDao.getAll();
    }
}
