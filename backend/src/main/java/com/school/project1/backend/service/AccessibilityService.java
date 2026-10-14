package com.school.project1.backend.service;

import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.needs.FacilityNeed;
import com.school.project1.backend.model.needs.NeedEvaluation;
import com.school.project1.backend.model.needs.SpecialNeed;
import com.school.project1.backend.model.needs.TimingNeed;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class AccessibilityService {

    public NeedEvaluation evaluate(SpecialNeed need, Facility facility) {
        Objects.requireNonNull(need, "need");
        Objects.requireNonNull(facility, "facility");
        return switch (need) {
            case FacilityNeed facilityNeed -> facilityNeed.evaluate(facility);
            case TimingNeed ignored -> NeedEvaluation.NOT_APPLICABLE;
        };
    }

    public NeedEvaluation evaluate(Collection<? extends SpecialNeed> needs, Facility facility) {
        Objects.requireNonNull(needs, "needs");
        Objects.requireNonNull(facility, "facility");
        boolean applicable = false;
        boolean unsatisfied = false;

        for (SpecialNeed need : needs) {
            switch (evaluate(need, facility)) {
                case UNSATISFIED -> unsatisfied = true;
                case SATISFIED -> applicable = true;
                case NOT_APPLICABLE -> { }
            }
        }

        if (unsatisfied) {
            return NeedEvaluation.UNSATISFIED;
        }
        return applicable ? NeedEvaluation.SATISFIED : NeedEvaluation.NOT_APPLICABLE;
    }
}
