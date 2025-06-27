package com.example.demo.controller;

import com.example.demo.entity.Officer;
import com.example.demo.service.OfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officers")
@Tag(name = "Officer Management", description = "APIs for officer registration, login, and management")
public class OfficerController {

    @Autowired
    private OfficerService officerService;

    @PostMapping("/register")
    @Operation(summary = "Register a new officer")
    public Officer register(@RequestBody Officer officer) {
        return officerService.registerOfficer(officer);
    }

    @PostMapping("/login")
    @Operation(summary = "Login officer with email and password")
    public Officer login(@RequestParam String email, @RequestParam String password) {
        return officerService.login(email, password);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get officer by ID")
    public Officer getById(@PathVariable Long id) {
        return officerService.getOfficerById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete officer by ID")
    public void delete(@PathVariable Long id) {
        officerService.deleteOfficer(id);
    }

    @GetMapping
    @Operation(summary = "List all officers")
    public List<Officer> getAll() {
        return officerService.getAllOfficers();
    }
} 
