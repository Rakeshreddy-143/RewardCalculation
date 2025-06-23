package com.rewards.retail.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RewardTransactionSummary {
    private LocalDate month;
    private int rewardPoints;
    private int totalSpent;
}
