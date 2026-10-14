package com.school.project1.backend.model.needs;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;

import java.util.Objects;

public abstract sealed class FacilityNeed extends SpecialNeed
    permits WheelchairAccessibilityNeed, ElevatorRequiredNeed, LevelAccessNeed,
    SeatingNeed, GuidingLineNeed, VisualInformationNeed, AuditoryInformationNeed {

    private final FacilityRole role;

    protected FacilityNeed(NeedType type, FacilityRole role) {
        super(type);
        this.role = Objects.requireNonNull(role, "role");
    }

    public final NeedEvaluation evaluate(Facility facility) {
        Objects.requireNonNull(facility, "facility");
        if (facility.getRole() != role) {
            return NeedEvaluation.NOT_APPLICABLE;
        }
        return isSatisfiedBy(facility)
            ? NeedEvaluation.SATISFIED
            : NeedEvaluation.UNSATISFIED;
    }

    protected abstract boolean isSatisfiedBy(Facility facility);
}
