package com.school.project1.backend.model.needs;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.facilities.Facility;

public class MobilityNeed extends SpecialNeed {
    public MobilityNeed(Need need) {
        super(need);
    }

    @Override
    public Boolean isAccessible(Facility facility) {
        Boolean accessible = true;

        if( requireLevelFloor && !facility.isLevelFloored() ){
            accessible = false;
        }

        if( requireSeating && !facility.hasSeating() ) {
            accessible = false;
        }

        if( requireWheelchairAccessible && !facility.isWheelchairAccessible() ) {
            accessible = false;
        }

        return accessible;
    }

    private Boolean requireWheelchairAccessible;

    private Boolean requireSeating;
    private Boolean requireLevelFloor;
}
