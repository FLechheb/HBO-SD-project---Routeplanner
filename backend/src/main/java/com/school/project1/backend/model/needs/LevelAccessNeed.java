package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class LevelAccessNeed extends FacilityNeed {

    public LevelAccessNeed() {
        super(NeedType.LEVEL_ACCESS_REQUIRED, FacilityRole.MOVEMENT);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility.isStepFree();
    }
}
