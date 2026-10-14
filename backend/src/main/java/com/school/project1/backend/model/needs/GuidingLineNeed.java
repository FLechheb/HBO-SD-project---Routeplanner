package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class GuidingLineNeed extends FacilityNeed {

    public GuidingLineNeed() {
        super(NeedType.GUIDING_LINE_REQUIRED, FacilityRole.MOVEMENT);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility.hasGuidingLine();
    }
}
