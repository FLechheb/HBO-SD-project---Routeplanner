package com.school.project1.backend.controllers;

import com.school.project1.backend.dto.needs.SpecialNeedDto;
import com.school.project1.backend.service.SpecialNeedService;
import com.school.project1.backend.service.mappers.SpecialNeedMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SpecialNeedController {

    private final SpecialNeedService specialNeedService;
    private final SpecialNeedMapper specialNeedMapper;

    public SpecialNeedController(SpecialNeedService specialNeedService, SpecialNeedMapper specialNeedMapper) {
        this.specialNeedMapper = specialNeedMapper;
        this.specialNeedService = specialNeedService;
    }

    @GetMapping("/needs")
    public ResponseEntity<List<SpecialNeedDto>> getNeeds() {

        return ResponseEntity.ok( specialNeedService.getAllNeeds().stream().map(specialNeedMapper::toDto).toList());
    }
}