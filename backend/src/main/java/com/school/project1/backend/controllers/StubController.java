package com.school.project1.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class StubController {

    @GetMapping("/stub")
    public ResponseEntity<Stub> getStub() {
        return ResponseEntity.ok(new Stub("This is a stub"));
    }
}