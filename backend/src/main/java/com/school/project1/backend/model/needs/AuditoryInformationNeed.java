package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class AuditoryInformationNeed extends FacilityNeed {

    public AuditoryInformationNeed() {
        super(NeedType.AUDITORY_INFORMATION_REQUIRED, FacilityRole.INFORMATION);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility.providesAuditoryInformation();
    }
}
