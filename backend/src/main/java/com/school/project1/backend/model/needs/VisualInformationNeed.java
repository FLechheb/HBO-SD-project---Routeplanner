package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class VisualInformationNeed extends FacilityNeed {

    public VisualInformationNeed() {
        super(NeedType.VISUAL_INFORMATION_REQUIRED, FacilityRole.INFORMATION);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility.providesVisualInformation();
    }
}
