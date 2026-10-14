package com.school.project1.backend.model.facilities;

public class InfoSpeaker extends Facility {

    @Override
    public FacilityRole getRole() {
        return FacilityRole.INFORMATION;
    }

    @Override
    public boolean providesAuditoryInformation() {
        return true;
    }
}
