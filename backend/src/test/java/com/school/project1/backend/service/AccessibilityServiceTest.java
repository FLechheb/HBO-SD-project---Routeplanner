package com.school.project1.backend.service;

import com.school.project1.backend.model.facilities.Elevator;
import com.school.project1.backend.model.facilities.InfoBoard;
import com.school.project1.backend.model.facilities.InfoSpeaker;
import com.school.project1.backend.model.facilities.Stairs;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SpecialNeed;
import com.school.project1.backend.repository.dao.NeedsDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
    "spring.datasource.generate-unique-name=true",
    "spring.docker.compose.enabled=false"
})
class AccessibilityServiceTest {

    @Autowired
    private AccessibilityService accessibilityService;

    @Autowired
    private NeedsDao needsDao;

    @Test
    void deafPersonCannotUseInfoSpeaker() {
        SpecialNeed deaf = findNeed(NeedType.DEAF);

        assertTrue(accessibilityService.isAccessible(deaf, new Stairs()));
        assertTrue(accessibilityService.isAccessible(deaf, new Elevator()));
        assertTrue(accessibilityService.isAccessible(deaf, new InfoBoard()));
        assertFalse(accessibilityService.isAccessible(deaf, new InfoSpeaker()));
    }

    @Test
    void blindPersonCannotUseInfoBoard() {
        SpecialNeed blind = findNeed(NeedType.BLIND);

        assertTrue(accessibilityService.isAccessible(blind, new InfoSpeaker()));
        assertFalse(accessibilityService.isAccessible(blind, new InfoBoard()));
        assertTrue(accessibilityService.isAccessible(blind, new Stairs()));
        assertTrue(accessibilityService.isAccessible(blind, new Elevator()));
    }

    @Test
    void wheelchairUserCanUseElevator() {
        SpecialNeed wheelchair = findNeed(NeedType.WHEELCHAIR_BOUND);

        assertTrue(accessibilityService.isAccessible(wheelchair, new Elevator()));
        assertFalse(accessibilityService.isAccessible(wheelchair, new Stairs()));
        assertFalse(accessibilityService.isAccessible(wheelchair, new InfoBoard()));
        assertFalse(accessibilityService.isAccessible(wheelchair, new InfoSpeaker()));
    }

    @Test
    void elderlyPersonCannotUseStairs() {
        SpecialNeed elderly = findNeed(NeedType.ELDERLY);

        assertFalse(accessibilityService.isAccessible(elderly, new Stairs()));
        assertTrue(accessibilityService.isAccessible(elderly, new Elevator()));
        assertTrue(accessibilityService.isAccessible(elderly, new InfoBoard()));
        assertTrue(accessibilityService.isAccessible(elderly, new InfoSpeaker()));
    }

    @Test
    void physicallyImpairedPersonCannotUseStairs() {
        SpecialNeed physicallyImpaired = findNeed(NeedType.PHYSICAL_IMPAIRED);

        assertFalse(accessibilityService.isAccessible(physicallyImpaired, new Stairs()));
        assertTrue(accessibilityService.isAccessible(physicallyImpaired, new Elevator()));
        assertTrue(accessibilityService.isAccessible(physicallyImpaired, new InfoBoard()));
        assertTrue(accessibilityService.isAccessible(physicallyImpaired, new InfoSpeaker()));
    }

    private SpecialNeed findNeed(NeedType type) {
        for (SpecialNeed need : needsDao.getAll()) {
            if (need.getNeed().type() == type) {
                return need;
            }
        }
        throw new IllegalStateException("Missing seeded need: " + type);
    }
}
