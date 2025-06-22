package com.rewards.retail.controller;

import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.service.RetailApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rewards")
public class RetailApplicationController {

    private final RetailApplicationService service;
    @Autowired
    public RetailApplicationController(RetailApplicationService service) {
        this.service = service;
    }
    @GetMapping("/{personId}")
    public RewardResponse reward(@PathVariable Long personId) {
        return service.calculate(personId);
    }
}


