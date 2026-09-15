package com.school.project1.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class StubController {

    @GetMapping("/stub")
    public ResponseEntity<Stub> getUser(){

        return ResponseEntity.ok( new Stub("This is a stub"));
    }
}