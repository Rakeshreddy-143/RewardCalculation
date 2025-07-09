package com.rewards.retail.service;

import com.rewards.retail.dto.MonthlyRewardSummary;
import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.dto.RewardTransactionSummary;
import com.rewards.retail.entity.BankTransaction;
import com.rewards.retail.entity.Customer;
import com.rewards.retail.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CustomerService customerService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
    }

    public RewardResponse calculateRewards(List<BankTransaction> transactions) {
        Map<Long, List<BankTransaction>> transactionsByCustomer = transactions.stream()
                .collect(Collectors.groupingBy(BankTransaction::getCustomerId));

        Map<Long, MonthlyRewardSummary> rewards = new HashMap<>();

        for (Map.Entry<Long, List<BankTransaction>> customerEntry : transactionsByCustomer.entrySet()) {
            Long customerId = customerEntry.getKey();
            List<BankTransaction> customerTransactions = customerEntry.getValue();

            MonthlyRewardSummary monthlySummary = calculateMonthlySummary(customerTransactions);
            Customer customerDetails = customerService.getCustomerById(customerId);
            monthlySummary.setName(customerDetails.getName());
            monthlySummary.setEmail(customerDetails.getEmail());
            monthlySummary.setPhoneNumber(customerDetails.getPhoneNumber());
            rewards.put(customerId, monthlySummary);
        }

        return new RewardResponse(rewards);
    }
    private MonthlyRewardSummary calculateMonthlySummary(List<BankTransaction> transactions) {
        Map<YearMonth, List<BankTransaction>> transactionsByMonth = groupTransactionsByMonth(transactions);
        return buildMonthlySummaries(transactionsByMonth);
    }

    private Map<YearMonth, List<BankTransaction>> groupTransactionsByMonth(List<BankTransaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(tr -> YearMonth.from(tr.getTransactionDate())));
    }

    private MonthlyRewardSummary buildMonthlySummaries(Map<YearMonth, List<BankTransaction>> transactionsByMonth) {
        List<RewardTransactionSummary> monthSummaries = transactionsByMonth.entrySet().stream()
                .map(entry -> {
                    YearMonth yearMonth = entry.getKey();
                    List<BankTransaction> monthTransactions = entry.getValue();

                    double totalSpent = monthTransactions.stream()
                            .mapToDouble(BankTransaction::getAmount).sum();

                    int rewardPoints = monthTransactions.stream()
                            .mapToInt(tr -> calculateRewardPoints((int) tr.getAmount()))
                            .sum();
                    return RewardTransactionSummary.builder()
                            .numberOfTransactions(monthTransactions.size())
                            .month(yearMonth.atDay(1))
                            .totalSpent(totalSpent)
                            .rewardPoints(rewardPoints)
                            .build();
                })
                .collect(Collectors.toList());

        int totalRewardPoints = monthSummaries.stream()
                .mapToInt(RewardTransactionSummary::getRewardPoints)
                .sum();

        return MonthlyRewardSummary.builder()
                .totalRewardPoints(totalRewardPoints)
                .transactions(monthSummaries)
                .build();
    }


    private int calculateRewardPoints(int amount) {
        return (amount > 100) ? (2 * (amount - 100)) + 50 : (amount >= 50) ? amount - 50 : 0;
    }

    public RewardResponse calculateRewardsForCustomer(Long customerId, LocalDate from, LocalDate to) {
        List<BankTransaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, from, to);

        return calculateRewards(transactions);
    }
}