package com.school.project1.backend.model.needs;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.facilities.Facility;

public class SensoryNeed extends SpecialNeed {
    public SensoryNeed(Need need) {
        super(need);
    }

    @Override
    public Boolean isAccessible(Facility facility) {
        boolean accessible = true;

        if( getNeed().visualAid() && !facility.hasVisualAid() ){
            accessible = false;
        }

        if(getNeed().auditoryAid() && !facility.hasAuditoryAid() ) {
            accessible = false;
        }

        return accessible;
    }
}
