package com.school.project1.backend.service;

import com.school.project1.backend.repository.dao.StopDAO;
import com.school.project1.backend.model.Stop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopService {

    private final StopDAO stopDAO;

    public StopService(StopDAO stopDAO) {
        this.stopDAO = stopDAO;
    }

    public List<Stop> getStops() {
        return stopDAO.findAllStops();
    }
}
