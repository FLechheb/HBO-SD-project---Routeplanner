package com.school.project1.backend.model.needs;

public final class SpecialNeedFactory {

    private SpecialNeedFactory() {
    }

    public static SpecialNeed create(NeedType type) {
        return switch (type) {
            case WHEELCHAIR_ACCESSIBLE -> new WheelchairAccessibilityNeed();
            case ELEVATOR_REQUIRED -> new ElevatorRequiredNeed();
            case LEVEL_ACCESS_REQUIRED -> new LevelAccessNeed();
            case SEATING_REQUIRED -> new SeatingNeed();
            case GUIDING_LINE_REQUIRED -> new GuidingLineNeed();
            case VISUAL_INFORMATION_REQUIRED -> new VisualInformationNeed();
            case AUDITORY_INFORMATION_REQUIRED -> new AuditoryInformationNeed();
            case EXTRA_TRANSFER_TIME -> new ExtraTransferTimeNeed();
        };
    }
}
