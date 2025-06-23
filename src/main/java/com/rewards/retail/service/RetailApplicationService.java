package com.rewards.retail.service;

import com.rewards.retail.dto.MonthlyRewardSummary;
import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.dto.RewardTransactionSummary;
import com.rewards.retail.dto.RewardRequest;
import com.rewards.retail.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RetailApplicationService {

    private final TransactionRepository transactionRepository;
    @Autowired
    public RetailApplicationService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public RewardResponse calculateRewards(List<RewardRequest> transactions) {
        Map<Long, Map<YearMonth, List<RewardRequest>>> grouped = transactions.stream()
                .collect(Collectors.groupingBy(
                        RewardRequest::getPersonId,
                        Collectors.groupingBy(tr -> YearMonth.from(tr.getTransactionDate()))
                ));

        Map<Long, MonthlyRewardSummary> rewards = new HashMap<>();

        for (Map.Entry<Long, Map<YearMonth, List<RewardRequest>>> personEntry : grouped.entrySet()) {
            List<RewardTransactionSummary> monthTransactions = new ArrayList<>();
            int totalRewardPoints = 0;

            for (Map.Entry<YearMonth, List<RewardRequest>> monthEntry : personEntry.getValue().entrySet()) {
                int totalSpent = monthEntry.getValue().stream().mapToInt(RewardRequest::getAmount).sum();
                int rewardPoints = monthEntry.getValue().stream()
                        .mapToInt(tr -> calculateRewardPoints(tr.getAmount()))
                        .sum();

                RewardTransactionSummary rt = new RewardTransactionSummary();
                rt.setMonth(monthEntry.getKey().atDay(1));
                rt.setTotalSpent(totalSpent);
                rt.setRewardPoints(rewardPoints);

                monthTransactions.add(rt);
                totalRewardPoints += rewardPoints;
            }

            MonthlyRewardSummary monthRewards = MonthlyRewardSummary.builder()
                    .totalRewardPoints(totalRewardPoints)
                    .transactions(monthTransactions)
                    .build();
            rewards.put(personEntry.getKey(), monthRewards);
        }

        return new RewardResponse(rewards);
    }


    private int calculateRewardPoints(int amount) {
        if (amount > 100) {
            return (2 * (amount - 100)) + 50;
        } else if (amount >= 50) {
            return amount - 50;
        } else {
            return 0;
        }
    }

    public RewardResponse calculateRewardsForCustomer(Long customerId, LocalDate from, LocalDate to, List<RewardRequest> transactions) {
        List<RewardRequest> filteredTransactions = transactions.stream()
                .filter(tr -> tr.getPersonId().equals(customerId) &&
                        (tr.getTransactionDate().isEqual(from) || tr.getTransactionDate().isAfter(from)) &&
                        (tr.getTransactionDate().isEqual(to) || tr.getTransactionDate().isBefore(to)))
                .collect(Collectors.toList());

        return calculateRewards(filteredTransactions);
    }
}