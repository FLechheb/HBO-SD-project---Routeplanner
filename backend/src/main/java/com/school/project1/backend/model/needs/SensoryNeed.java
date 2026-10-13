package com.school.project1.backend.model.needs;

import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.facilities.Facility;

public class SensoryNeed extends SpecialNeed {
    public SensoryNeed(Need need) {
        super(need);
    }

    @Override
    public Boolean isAccessible(Facility facility) {
        return switch (getNeed().type()) {
            case BLIND -> facility.isUsableWithoutVision();
            case DEAF -> facility.isUsableWithoutHearing();
            default -> throw new IllegalStateException("Not a sensory need: " + getNeed().type());
        };
    }
}
