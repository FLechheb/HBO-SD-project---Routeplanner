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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccessibilityServiceTest {

    private final AccessibilityService service = new AccessibilityService();

    private record RuleCase(NeedType type, Facility supported, Facility unsupported,
                            Facility unrelated) {
    }

    @Test
    void evaluatesRequirementRulesAndIgnoresUnrelatedFacilityRoles() {
        // Arrange
        Elevator seatedElevator = mock(Elevator.class);
        when(seatedElevator.getRole()).thenReturn(FacilityRole.MOVEMENT);
        when(seatedElevator.hasSeating()).thenReturn(true);
        Stairs guidedStairs = mock(Stairs.class);
        when(guidedStairs.getRole()).thenReturn(FacilityRole.MOVEMENT);
        when(guidedStairs.hasGuidingLine()).thenReturn(true);
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
            // Arrange
            SpecialNeed need = SpecialNeedFactory.create(rule.type());

            // Act
            NeedEvaluation supported = service.evaluate(need, rule.supported());
            NeedEvaluation unsupported = service.evaluate(need, rule.unsupported());
            NeedEvaluation unrelated = service.evaluate(need, rule.unrelated());

            // Assert
            assertEquals(SATISFIED, supported, rule.type().name());
            assertEquals(UNSATISFIED, unsupported, rule.type().name());
            assertEquals(NOT_APPLICABLE, unrelated, rule.type().name());
        });
    }

    @Test
    void combinesApplicableRequirementsWithAnd() {
        // Arrange
        List<SpecialNeed> needs = needs(
            NeedType.WHEELCHAIR_ACCESSIBLE,
            NeedType.LEVEL_ACCESS_REQUIRED,
            NeedType.ELEVATOR_REQUIRED,
            NeedType.VISUAL_INFORMATION_REQUIRED
        );
        List<SpecialNeed> seatingNeeds = needs(
            NeedType.WHEELCHAIR_ACCESSIBLE, NeedType.SEATING_REQUIRED
        );
        List<SpecialNeed> informationNeeds = needs(
            NeedType.VISUAL_INFORMATION_REQUIRED, NeedType.AUDITORY_INFORMATION_REQUIRED
        );
        Elevator elevator = new Elevator();
        Stairs stairs = new Stairs();
        InfoBoard infoBoard = new InfoBoard();
        InfoBoard multimodalInformation = mock(InfoBoard.class);
        when(multimodalInformation.getRole()).thenReturn(FacilityRole.INFORMATION);
        when(multimodalInformation.providesVisualInformation()).thenReturn(true);
        when(multimodalInformation.providesAuditoryInformation()).thenReturn(true);

        // Act
        NeedEvaluation elevatorResult = service.evaluate(needs, elevator);
        NeedEvaluation stairsResult = service.evaluate(needs, stairs);
        NeedEvaluation infoBoardResult = service.evaluate(needs, infoBoard);
        NeedEvaluation seatingResult = service.evaluate(seatingNeeds, elevator);
        NeedEvaluation visualOnlyResult = service.evaluate(informationNeeds, infoBoard);
        NeedEvaluation multimodalResult = service.evaluate(informationNeeds, multimodalInformation);

        // Assert
        assertEquals(SATISFIED, elevatorResult);
        assertEquals(UNSATISFIED, stairsResult);
        assertEquals(SATISFIED, infoBoardResult);
        assertEquals(UNSATISFIED, seatingResult);
        assertEquals(UNSATISFIED, visualOnlyResult);
        assertEquals(SATISFIED, multimodalResult);
    }

    @Test
    void timingAndEmptySelectionsDoNotClaimFacilitySatisfaction() {
        // Arrange
        List<SpecialNeed> empty = List.of();
        SpecialNeed timingNeed = SpecialNeedFactory.create(NeedType.EXTRA_TRANSFER_TIME);
        List<SpecialNeed> timingOnly = needs(NeedType.EXTRA_TRANSFER_TIME);
        List<SpecialNeed> mixed = needs(NeedType.EXTRA_TRANSFER_TIME, NeedType.WHEELCHAIR_ACCESSIBLE);
        Elevator elevator = new Elevator();
        Stairs stairs = new Stairs();
        InfoBoard infoBoard = new InfoBoard();

        // Act
        NeedEvaluation emptyResult = service.evaluate(empty, elevator);
        NeedEvaluation elevatorTimingResult = service.evaluate(timingNeed, elevator);
        NeedEvaluation stairsTimingResult = service.evaluate(timingNeed, stairs);
        NeedEvaluation timingOnlyResult = service.evaluate(timingOnly, infoBoard);
        NeedEvaluation mixedElevatorResult = service.evaluate(mixed, elevator);
        NeedEvaluation mixedStairsResult = service.evaluate(mixed, stairs);
        NeedEvaluation mixedInfoBoardResult = service.evaluate(mixed, infoBoard);

        // Assert
        assertEquals(NOT_APPLICABLE, emptyResult);
        assertEquals(NOT_APPLICABLE, elevatorTimingResult);
        assertEquals(NOT_APPLICABLE, stairsTimingResult);
        assertEquals(NOT_APPLICABLE, timingOnlyResult);
        assertEquals(SATISFIED, mixedElevatorResult);
        assertEquals(UNSATISFIED, mixedStairsResult);
        assertEquals(NOT_APPLICABLE, mixedInfoBoardResult);
    }

    private List<SpecialNeed> needs(NeedType... types) {
        return Stream.of(types).map(SpecialNeedFactory::create).toList();
    }

    private Facility movement(boolean stepFree, boolean wheelchairAccessible) {
        Facility facility = mock(Facility.class);
        when(facility.getRole()).thenReturn(FacilityRole.MOVEMENT);
        when(facility.isStepFree()).thenReturn(stepFree);
        when(facility.isWheelchairAccessible()).thenReturn(wheelchairAccessible);
        return facility;
    }
}
