package com.rewards.retail.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RewardTransactionSummary {
    private LocalDate month;
    private int rewardPoints;
    private double totalSpent;
    private int numberOfTransactions;
}
