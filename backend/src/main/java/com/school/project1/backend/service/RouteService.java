package com.school.project1.backend.service;

import com.school.project1.backend.clients.OtpClient;
import com.school.project1.backend.model.RoutePlanRequest;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    private final OtpClient otpClient;

    public RouteService(OtpClient otpClient) {
        this.otpClient = otpClient;
    }

    public String planRoute(RoutePlanRequest request) {
        return otpClient.planRoute(request);
    }
}
