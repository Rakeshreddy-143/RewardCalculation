package com.rewards.retail.service;

import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RetailApplicationService {

    private final TransactionRepository repo;
    @Autowired
    public RetailApplicationService(TransactionRepository repo) {
        this.repo = repo;
    }

    public RewardResponse calculate(Long personId) {
        LocalDate since = LocalDate.now().minusMonths(1);

        int total = repo.totalSpentInMonth(personId, since, LocalDate.now());

        int reward = (total > 100) ? ((2 * (total - 100)) + 50) : (total >= 50) ? (total - 50) : 0;

        return new RewardResponse(personId, total, reward);
    }
}