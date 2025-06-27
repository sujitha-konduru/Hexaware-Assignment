package com.example.demo.controller;

import com.example.demo.entity.Proposal;
import com.example.demo.service.ProposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposals")
@Tag(name = "Policy Proposal Management", description = "APIs for vehicle insurance proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping("/submit/{userId}")
    @Operation(summary = "Submit a new policy proposal for a user")
    public Proposal submitProposal(@PathVariable Long userId, @RequestBody Proposal proposal) {
        return proposalService.submitProposal(userId, proposal);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all proposals submitted by a user")
    public List<Proposal> getUserProposals(@PathVariable Long userId) {
        return proposalService.getProposalsByUser(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get proposal by ID")
    public Proposal getById(@PathVariable Long id) {
        return proposalService.getProposalById(id);
    }
}
