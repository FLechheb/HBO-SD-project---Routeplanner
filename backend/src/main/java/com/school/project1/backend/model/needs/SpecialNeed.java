package com.school.project1.backend.model.needs;

import java.util.Objects;

public abstract sealed class SpecialNeed permits FacilityNeed, TimingNeed {

    private final NeedType type;

    protected SpecialNeed(NeedType type) {
        this.type = Objects.requireNonNull(type, "type");
    }

    public final NeedType getType() {
        return type;
    }

    public final String getDescription() {
        return type.getDescription();
    }
}
