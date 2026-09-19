package com.school.project1.backend.services;

import com.school.project1.backend.clients.*;
import org.springframework.stereotype.Service;
import com.school.project1.backend.models.RoutePlanRequest;

@Service
public class RouteService {
    private final OtpClient otpClient;

    public RouteService(OtpClient otpClient) {
        this.otpClient = otpClient;
    }

    public String planRoute(RoutePlanRequest request) {
        return otpClient.planRoute(request);
        // later: resultaat combineren met instellingen uit SettingsDao
    }
}
