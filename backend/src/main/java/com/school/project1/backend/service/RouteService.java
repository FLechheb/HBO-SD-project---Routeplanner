package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.dao.RouteDAO;
import com.school.project1.backend.model.RoutePlanRequest;
import com.school.project1.backend.model.Stop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteDAO routeDAO;
    private final OtpClient otpClient;

    public RouteService(RouteDAO routeDAO, OtpClient otpClient) {
        this.routeDAO = routeDAO;
        this.otpClient = otpClient;
    }

    public List<Stop> getStops() {
      return routeDAO.findAllStops();
    }

    public String planRoute(RoutePlanRequest request) {
      return otpClient.planRoute(request);
    }
}
