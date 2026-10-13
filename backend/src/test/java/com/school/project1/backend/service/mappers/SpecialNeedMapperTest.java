package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.needs.MobilityNeed;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SensoryNeed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialNeedMapperTest {

    private final SpecialNeedMapper mapper;
    public SpecialNeedMapperTest(SpecialNeedMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    void toDto() {
        Need need = new Need(NeedType.BLIND, true, true, true, true, true);
        Need need2 = new Need(NeedType.WHEELCHAIR_BOUND, false, false, false, false, false);

        SensoryNeed sNeed = new SensoryNeed(need);
        MobilityNeed mNeed = new MobilityNeed(need2);

        SpecialNeedDto snDto = mapper.toDto(sNeed);
        SpecialNeedDto mDto = mapper.toDto(mNeed);

        assertEquals(NeedType.BLIND, snDto.type());
        assertTrue( snDto.extraTime());
        assertTrue( snDto.seating());
        assertTrue( snDto.levelFloor());
        assertTrue( snDto.visualAid());
        assertTrue( snDto.auditoryAid());

        assertEquals(NeedType.WHEELCHAIR_BOUND, mDto.type());
        assertFalse( mDto.extraTime());
        assertFalse( mDto.seating());
        assertFalse( mDto.levelFloor());
        assertFalse( mDto.visualAid());
        assertFalse( mDto.auditoryAid());
    }

    @Test
    void description() {
    }
}