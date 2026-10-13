package com.school.project1.backend.model.needs;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.facilities.Facility;

public abstract class SpecialNeed {

    private final Need need;

    public SpecialNeed(Need need) {
        this.need = need;
    }

    public Need getNeed() {
        return this.need;
    }

    public String getDescription(){
        return getNeed().type().getDescription();
    }

    public abstract Boolean isAccessible(Facility facility);

    Boolean requireExtraTime; //Pas relevant met de route berekenen
}
