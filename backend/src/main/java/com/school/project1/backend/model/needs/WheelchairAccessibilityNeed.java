package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class WheelchairAccessibilityNeed extends FacilityNeed {

    public WheelchairAccessibilityNeed() {
        super(NeedType.WHEELCHAIR_ACCESSIBLE, FacilityRole.MOVEMENT);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility.isWheelchairAccessible();
    }
}
