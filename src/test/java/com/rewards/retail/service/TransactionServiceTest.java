package com.rewards.retail.service;

import com.rewards.retail.dto.MonthlyRewardSummary;
import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.dto.RewardTransactionSummary;
import com.rewards.retail.entity.BankTransaction;
import com.rewards.retail.entity.Customer;
import com.rewards.retail.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private CustomerService customerService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        customerService = mock(CustomerService.class);
        transactionService = new TransactionService(transactionRepository, customerService);
    }

    @Test
    void testCalculateRewards_singleCustomer_singleTransaction() {
        BankTransaction transaction = BankTransaction.builder()
                .customerId(1L)
                .amount(120)
                .transactionDate(LocalDate.of(2025, 5, 15))
                .build();

        Customer mockCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .build();

        when(customerService.getCustomerById(1L)).thenReturn(mockCustomer);

        RewardResponse response = transactionService.calculateRewards(List.of(transaction));

        assertNotNull(response);
        assertTrue(response.getRewards().containsKey(1L));

        MonthlyRewardSummary summary = response.getRewards().get(1L);
        assertEquals(90, summary.getTotalRewardPoints());
        assertEquals("John Doe", summary.getName());
        assertEquals("john@example.com", summary.getEmail());
        assertEquals("1234567890", summary.getPhoneNumber());

        List<RewardTransactionSummary> transactions = summary.getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(120.0, transactions.get(0).getTotalSpent());
        assertEquals(90, transactions.get(0).getRewardPoints());
    }

    @Test
    void testCalculateRewards_noTransactions() {
        RewardResponse response = transactionService.calculateRewards(Collections.emptyList());
        assertNotNull(response);
        assertTrue(response.getRewards().isEmpty());
    }

    @Test
    void testCalculateRewardsForCustomer_withDateRange() {
        Long customerId = 2L;
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 12, 31);

        BankTransaction transaction = BankTransaction.builder()
                .customerId(customerId)
                .amount(75)
                .transactionDate(LocalDate.of(2025, 3, 10))
                .build();

        Customer customer = Customer.builder()
                .id(customerId)
                .name("Alice")
                .email("alice@example.com")
                .phoneNumber("9876543210")
                .build();

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, from, to))
                .thenReturn(List.of(transaction));

        when(customerService.getCustomerById(customerId)).thenReturn(customer);

        RewardResponse response = transactionService.calculateRewardsForCustomer(customerId, from, to);

        assertNotNull(response);
        assertEquals(1, response.getRewards().size());

        MonthlyRewardSummary summary = response.getRewards().get(customerId);
        assertEquals(25, summary.getTotalRewardPoints());
    }
}
