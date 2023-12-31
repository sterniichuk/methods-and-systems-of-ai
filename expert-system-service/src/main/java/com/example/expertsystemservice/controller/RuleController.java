package com.example.expertsystemservice.controller;

import com.example.expertsystemservice.domain.*;
import com.example.expertsystemservice.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expert-system/rule")
@CrossOrigin(origins = {"http://localhost:3000"})
public class RuleController {
    private final RuleService service;

    //method to retrieve a rule and its hierarchy at a given depth
    @GetMapping("/{id}/{depth}")
    public RuleDTO getRule(@PathVariable long id, @PathVariable long depth) {
        return service.getRule(GetRuleRequest.builder()
                        .depth(depth)
                        .id(id)
                .build());
    }
    @PutMapping("/formula/{id}/{newFormula}")
    public RuleDTO updateFormula(@PathVariable long id, @PathVariable String newFormula) {
        return service.updateFormula(id, newFormula);
    }
    @PutMapping("action/formula/{id}/{newFormula}")
    public ActionDTO updateActionFormula(@PathVariable long id, @PathVariable String newFormula) {
        return service.updateActionFormula(id, newFormula);
    }

    //create a new rule. If the rule uses new rules as branches
    // they should be provided in the list(field of request body) too.
    // if a rule uses existing rules as branches, then the branches must contain the ID OR the name of the existing rule
    // So, it means that each element of the list has 2 as maximum height of its hierarchy
    @PostMapping
    public List<Long> postRules(@RequestBody @Valid PostRuleRequest request) {
        return service.createNewRule(request);
    }

    @PutMapping("/connect-actions")
    public List<Long> connectActionsToRule(@RequestBody @Valid ConnectActionsToRuleRequest request) {
        return service.connectActionsToRule(request);
    }

    @DeleteMapping("/{id}")
    public long deleteRule(@PathVariable long id) {
        return service.delete(id);
    }

    @DeleteMapping("/all-hierarchy/{id}/{depth}")
    public List<Long> deleteAllRuleHierarchy(@PathVariable long id, @PathVariable long depth) {
        return service.deleteAll(id, depth);
    }

    @DeleteMapping("/all")
    public long deleteAll() {
        return service.deleteAll();
    }
}
