package com.school.project1.backend.service;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.needs.SpecialNeed;
import org.springframework.stereotype.Service;

@Service
public class AccessibilityService {

    public Boolean isAccessible(SpecialNeed specialNeed, Facility facility) {



        return specialNeed.isAccessible(facility);
    }
}
