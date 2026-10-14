package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Elevator;
import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

public final class ElevatorRequiredNeed extends FacilityNeed {

    public ElevatorRequiredNeed() {
        super(NeedType.ELEVATOR_REQUIRED, FacilityRole.MOVEMENT);
    }

    @Override
    protected boolean isSatisfiedBy(Facility facility) {
        return facility instanceof Elevator;
    }
}
