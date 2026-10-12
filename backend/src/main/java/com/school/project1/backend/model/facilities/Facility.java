package com.school.project1.backend.model.facilities;

import com.school.project1.backend.model.needs.SpecialNeed;

public abstract class Facility { //TODO: op zelfde manier als facility opbouwen (niet elke lift is bv hetzelfde (wel/geen stoel in de lift bv))
    boolean isLevelFloored = true;
    boolean hasSeating = false;
    boolean hasVisualAid = false;
    boolean hasAuditoryAid = false;
    boolean isWheelchairAccessible = false;

    public boolean isLevelFloored() {
        return isLevelFloored;
    }
    public boolean hasSeating() {
        return hasSeating;
    }

    public boolean hasVisualAid() {
        return hasVisualAid;
    }

    public boolean hasAuditoryAid() {
        return hasAuditoryAid;
    }

    public boolean isWheelchairAccessible(){
        return isWheelchairAccessible;
    }
}
