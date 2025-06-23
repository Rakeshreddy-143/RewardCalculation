package com.rewards.retail.controller;

import com.rewards.retail.dto.MonthlyRewardSummary;
import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.dto.RewardRequest;
import com.rewards.retail.service.RetailApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class RetailApplicationController {

    private final RetailApplicationService service;
    @Autowired
    public RetailApplicationController(RetailApplicationService service) {
        this.service = service;
    }

    @PostMapping("/rewards/summary")
    public RewardResponse reward(@Valid @RequestBody List<@Valid RewardRequest> transactions) {
        return service.calculateRewards(transactions);

    }


    @PostMapping("/rewards/customer/{customerId}")
    public RewardResponse rewardForCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestBody List<RewardRequest> transactions) {
        return service.calculateRewardsForCustomer(customerId, from, to, transactions);
    }
}


