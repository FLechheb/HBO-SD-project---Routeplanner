package com.school.project1.backend.model.facilities;

public class Elevator extends Facility {

    @Override
    public FacilityRole getRole() {
        return FacilityRole.MOVEMENT;
    }

    @Override
    public boolean isStepFree() {
        return true;
    }

    @Override
    public boolean isWheelchairAccessible() {
        return true;
    }

}