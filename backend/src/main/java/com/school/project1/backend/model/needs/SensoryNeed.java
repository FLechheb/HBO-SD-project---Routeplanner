package com.school.project1.backend.model.needs;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.facilities.Facility;

public class SensoryNeed extends SpecialNeed {
    public SensoryNeed(Need need) {
        super(need);
    }

    @Override
    public Boolean isAccessible(Facility facility) {
        Boolean accessible = true;

        if( requireVisualAid && !facility.hasVisualAid() ){
            accessible = false;
        }

        if( requireAuditoryAid && !facility.hasAuditoryAid() ) {
            accessible = false;
        }

        return accessible;
    }

    private Boolean requireVisualAid;
    private Boolean requireAuditoryAid;
}
