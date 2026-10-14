package com.school.project1.backend.service;

import com.school.project1.backend.model.facilities.Elevator;
import com.school.project1.backend.model.facilities.Facility;
import com.school.project1.backend.model.facilities.FacilityRole;
import com.school.project1.backend.model.facilities.InfoBoard;
import com.school.project1.backend.model.facilities.InfoSpeaker;
import com.school.project1.backend.model.facilities.Stairs;
import com.school.project1.backend.model.needs.NeedEvaluation;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SpecialNeed;
import com.school.project1.backend.model.needs.SpecialNeedFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static com.school.project1.backend.model.needs.NeedEvaluation.NOT_APPLICABLE;
import static com.school.project1.backend.model.needs.NeedEvaluation.SATISFIED;
import static com.school.project1.backend.model.needs.NeedEvaluation.UNSATISFIED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessibilityServiceTest {

    private final AccessibilityService service = new AccessibilityService();

    private record RuleCase(NeedType type, Facility supported, Facility unsupported,
                            Facility unrelated) {
    }

    @Test
    void evaluatesRequirementRulesAndIgnoresUnrelatedFacilityRoles() {
        Facility seatedElevator = new Elevator() {
            @Override
            public boolean hasSeating() {
                return true;
            }
        };
        Facility guidedStairs = new Stairs() {
            @Override
            public boolean hasGuidingLine() {
                return true;
            }
        };
        Stream.of(
            new RuleCase(NeedType.WHEELCHAIR_ACCESSIBLE,
                movement(false, true), new Stairs(), new InfoBoard()),
            new RuleCase(NeedType.ELEVATOR_REQUIRED,
                new Elevator(), movement(true, true), new InfoSpeaker()),
            new RuleCase(NeedType.LEVEL_ACCESS_REQUIRED,
                movement(true, false), new Stairs(), new InfoBoard()),
            new RuleCase(NeedType.SEATING_REQUIRED,
                seatedElevator, new Elevator(), new InfoSpeaker()),
            new RuleCase(NeedType.GUIDING_LINE_REQUIRED,
                guidedStairs, new Stairs(), new InfoBoard()),
            new RuleCase(NeedType.VISUAL_INFORMATION_REQUIRED,
                new InfoBoard(), new InfoSpeaker(), new Elevator()),
            new RuleCase(NeedType.AUDITORY_INFORMATION_REQUIRED,
                new InfoSpeaker(), new InfoBoard(), new Stairs())
        ).forEach(rule -> {
            SpecialNeed need = SpecialNeedFactory.create(rule.type());
            assertEquals(SATISFIED, service.evaluate(need, rule.supported()), rule.type().name());
            assertEquals(UNSATISFIED, service.evaluate(need, rule.unsupported()), rule.type().name());
            assertEquals(NOT_APPLICABLE, service.evaluate(need, rule.unrelated()), rule.type().name());
        });
    }

    @Test
    void combinesApplicableRequirementsWithAnd() {
        List<SpecialNeed> needs = needs(
            NeedType.WHEELCHAIR_ACCESSIBLE,
            NeedType.LEVEL_ACCESS_REQUIRED,
            NeedType.ELEVATOR_REQUIRED,
            NeedType.VISUAL_INFORMATION_REQUIRED
        );
        assertEquals(SATISFIED, service.evaluate(needs, new Elevator()));
        assertEquals(UNSATISFIED, service.evaluate(needs, new Stairs()));
        assertEquals(SATISFIED, service.evaluate(needs, new InfoBoard()));

        assertEquals(UNSATISFIED, service.evaluate(
            needs(NeedType.WHEELCHAIR_ACCESSIBLE, NeedType.SEATING_REQUIRED),
            new Elevator()
        ));
        List<SpecialNeed> informationNeeds = needs(
            NeedType.VISUAL_INFORMATION_REQUIRED, NeedType.AUDITORY_INFORMATION_REQUIRED
        );
        assertEquals(UNSATISFIED, service.evaluate(informationNeeds, new InfoBoard()));
        Facility multimodalInformation = new InfoBoard() {
            @Override
            public boolean providesAuditoryInformation() {
                return true;
            }
        };
        assertEquals(SATISFIED, service.evaluate(informationNeeds, multimodalInformation));
    }

    @Test
    void timingAndEmptySelectionsDoNotClaimFacilitySatisfaction() {
        assertEquals(NOT_APPLICABLE, service.evaluate(List.of(), new Elevator()));
        assertEquals(NOT_APPLICABLE, evaluate(NeedType.EXTRA_TRANSFER_TIME, new Elevator()));
        assertEquals(NOT_APPLICABLE, evaluate(NeedType.EXTRA_TRANSFER_TIME, new Stairs()));
        assertEquals(NOT_APPLICABLE, service.evaluate(
            needs(NeedType.EXTRA_TRANSFER_TIME), new InfoBoard()
        ));

        List<SpecialNeed> mixed = needs(NeedType.EXTRA_TRANSFER_TIME, NeedType.WHEELCHAIR_ACCESSIBLE);
        assertEquals(SATISFIED, service.evaluate(mixed, new Elevator()));
        assertEquals(UNSATISFIED, service.evaluate(mixed, new Stairs()));
        assertEquals(NOT_APPLICABLE, service.evaluate(mixed, new InfoBoard()));
    }

    private NeedEvaluation evaluate(NeedType type, Facility facility) {
        return service.evaluate(SpecialNeedFactory.create(type), facility);
    }

    private List<SpecialNeed> needs(NeedType... types) {
        return Stream.of(types).map(SpecialNeedFactory::create).toList();
    }

    private Facility movement(boolean stepFree, boolean wheelchairAccessible) {
        return new Facility() {
            @Override
            public FacilityRole getRole() {
                return FacilityRole.MOVEMENT;
            }

            @Override
            public boolean isStepFree() {
                return stepFree;
            }

            @Override
            public boolean isWheelchairAccessible() {
                return wheelchairAccessible;
            }
        };
    }
}
