package com.school.project1.backend.model.needs;

public abstract sealed class TimingNeed extends SpecialNeed permits ExtraTransferTimeNeed {

    protected TimingNeed(NeedType type) {
        super(type);
    }
}
