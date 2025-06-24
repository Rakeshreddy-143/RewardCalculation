package com.rewards.retail;

import com.rewards.retail.dto.MonthlyRewardSummary;
import com.rewards.retail.dto.RewardRequest;
import com.rewards.retail.dto.RewardResponse;
import com.rewards.retail.repository.TransactionRepository;
import com.rewards.retail.service.RetailApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SpringBootTest
class RetailApplicationTests {
	private RetailApplicationService service;


	@BeforeEach
	void setUp() {
		this.service = new RetailApplicationService(mock(TransactionRepository.class));
	}

	@Test
	void testCalculateRewards_SingleTransaction() {
		RewardRequest req = new RewardRequest();
		req.setPersonId(1L);
		req.setAmount(120);
		req.setTransactionDate(LocalDate.of(2024, 6, 1));

		RewardResponse response = service.calculateRewards(Collections.singletonList(req));
		Map<Long, MonthlyRewardSummary> rewards = response.getRewards();

		assertTrue(rewards.containsKey(1L));
		assertEquals(90, rewards.get(1L).getTotalRewardPoints());
	}

	@Test
	void testCalculateRewards_MultipleCustomers() {
		RewardRequest req1 = new RewardRequest();
		req1.setPersonId(1L);
		req1.setAmount(120);
		req1.setTransactionDate(LocalDate.of(2024, 6, 1));

		RewardRequest req2 = new RewardRequest();
		req2.setPersonId(2L);
		req2.setAmount(60);
		req2.setTransactionDate(LocalDate.of(2024, 6, 2));

		RewardResponse response = service.calculateRewards(Arrays.asList(req1, req2));
		assertEquals(2, response.getRewards().size());
		assertEquals(90, response.getRewards().get(1L).getTotalRewardPoints());
		assertEquals(10, response.getRewards().get(2L).getTotalRewardPoints());
	}

	@Test
	void testCalculateRewardsForCustomer_Filtering() {
		RewardRequest req1 = new RewardRequest();
		req1.setPersonId(1L);
		req1.setAmount(120);
		req1.setTransactionDate(LocalDate.of(2024, 6, 1));

		RewardRequest req2 = new RewardRequest();
		req2.setPersonId(1L);
		req2.setAmount(80);
		req2.setTransactionDate(LocalDate.of(2024, 5, 1));

		RewardRequest req3 = new RewardRequest();
		req3.setPersonId(2L);
		req3.setAmount(200);
		req3.setTransactionDate(LocalDate.of(2024, 6, 10));

		List<RewardRequest> all = Arrays.asList(req1, req2, req3);

		RewardResponse response = service.calculateRewardsForCustomer(
				1L,
				LocalDate.of(2024, 6, 1),
				LocalDate.of(2024, 6, 30),
				all
		);
		assertEquals(1, response.getRewards().size());
		assertEquals(90, response.getRewards().get(1L).getTotalRewardPoints());
	}

	@Test
	void testCalculateRewardsForCustomer_NoTransactions() {
		List<RewardRequest> empty = Collections.emptyList();
		RewardResponse response = service.calculateRewardsForCustomer(
				1L,
				LocalDate.of(2024, 6, 1),
				LocalDate.of(2024, 6, 30),
				empty
		);
		assertTrue(response.getRewards().isEmpty());
	}


}