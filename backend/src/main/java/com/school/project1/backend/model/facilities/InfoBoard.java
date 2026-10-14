package com.school.project1.backend.model.facilities;

public class InfoBoard extends Facility {

    @Override
    public FacilityRole getRole() {
        return FacilityRole.INFORMATION;
    }

    @Override
    public boolean providesVisualInformation() {
        return true;
    }
}
