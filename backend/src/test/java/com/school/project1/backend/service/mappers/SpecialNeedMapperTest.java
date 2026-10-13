package com.school.project1.backend.service.mappers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.entity.Need;
import com.school.project1.backend.model.needs.MobilityNeed;
import com.school.project1.backend.model.needs.NeedType;
import com.school.project1.backend.model.needs.SensoryNeed;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class SpecialNeedMapperTest {

    private final SpecialNeedMapper mapper = Mappers.getMapper(SpecialNeedMapper.class);

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
    void fromDto() {
        SpecialNeedDto dto = new SpecialNeedDto(NeedType.BLIND, true, true, true, true, true);
        Need need = mapper.toNeed(dto);

        assertEquals(NeedType.BLIND, need.type());
        assertTrue( need.extraTime());
        assertTrue( need.seating());
        assertTrue( need.levelFloor());
        assertTrue( need.visualAid());
        assertTrue( need.auditoryAid());
    }

    @Test
    void toNeed() {
        SpecialNeedDto dto = new SpecialNeedDto(NeedType.WHEELCHAIR_BOUND, false, false, false, false, false);
        Need need = mapper.toNeed(dto);

        assertEquals(NeedType.WHEELCHAIR_BOUND, need.type());
        assertFalse( need.extraTime());
        assertFalse( need.seating());
        assertFalse( need.levelFloor());
        assertFalse( need.visualAid());
        assertFalse( need.auditoryAid());
    }
}