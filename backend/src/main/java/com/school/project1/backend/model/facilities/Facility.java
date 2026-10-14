package com.school.project1.backend.model.facilities;

public abstract class Facility {

    public abstract FacilityRole getRole();

    public boolean hasGuidingLine() {
        return false;
    }

    public boolean isStepFree() {
        return false;
    }

    public boolean hasSeating() {
        return false;
    }

    public boolean providesVisualInformation() {
        return false;
    }

    public boolean providesAuditoryInformation() {
        return false;
    }

    public boolean isWheelchairAccessible() {
        return false;
    }
}
