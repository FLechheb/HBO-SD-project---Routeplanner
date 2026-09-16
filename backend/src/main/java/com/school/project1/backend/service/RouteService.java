package com.school.project1.backend.service;

import com.school.project1.backend.dao.RouteDAO;
import com.school.project1.backend.model.Stop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteDAO routeDAO;

    public RouteService(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    public List<Stop> getStops() {
      return routeDAO.findAllStops();
    }
}