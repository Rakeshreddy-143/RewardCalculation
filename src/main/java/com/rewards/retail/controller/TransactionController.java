package com.rewards.retail.controller;

import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.service.TransactionService;
import com.rewards.retail.validation.RewardRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@Validated
public class TransactionController {

    private final TransactionService service;

    private final RewardRequestValidator validator;
    @Autowired
    public TransactionController(TransactionService service, RewardRequestValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @GetMapping("/rewards/customer/{customerId}")
    public ResponseEntity<?> rewardForCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestParam(value = "from",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {


        validator.validate(customerId, from, to);
        if (from == null && to == null) {
            from = LocalDate.now().minusMonths(3);
            to = LocalDate.now();
        }
        RewardResponse response = service.calculateRewardsForCustomer(customerId, from, to);
        if (response.getRewards().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No rewards found for the given criteria.");
        }
        return ResponseEntity.ok(response);
    }
}


