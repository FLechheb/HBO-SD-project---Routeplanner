package com.school.project1.backend.model.facilities;

public class Stairs extends Facility {

    @Override
    public FacilityRole getRole() {
        return FacilityRole.MOVEMENT;
    }
}
