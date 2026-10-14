package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class SeatingNeed extends FacilityNeed {

    public SeatingNeed() {
        super(NeedType.SEATING_REQUIRED, FacilityRole.MOVEMENT);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility.hasSeating();
    }
}
