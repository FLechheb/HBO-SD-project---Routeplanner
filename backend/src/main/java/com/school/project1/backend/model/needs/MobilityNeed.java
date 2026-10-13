package com.school.project1.backend.model.needs;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.facilities.Facility;

public class MobilityNeed extends SpecialNeed {
    public MobilityNeed(Need need) {
        super(need);
    }

    @Override
    public Boolean isAccessible(Facility facility) {
        boolean accessible = true;

        if(getNeed().levelFloor() && !facility.isLevelFloored() ){
            accessible = false;
        }

        if( getNeed().type() == NeedType.WHEELCHAIR_BOUND
            && (!facility.isLevelFloored() || !facility.isWheelchairAccessible()) ) {
            accessible = false;
        }

        return accessible;
    }
}
