package com.rewards.retail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardResponse {
    private Map<Long, MonthlyRewardSummary> rewards;
}
