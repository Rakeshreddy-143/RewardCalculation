package com.rewards.retail.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class MonthlyRewardSummary {

    private List<RewardTransactionSummary> transactions;
    private int totalRewardPoints;

    private String name;
    private String email;
    private String phoneNumber;

}
