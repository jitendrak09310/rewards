package com.RewardsManagement.rewards.testService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.RewardsManagement.rewards.dto.CustomerTransactionResponse;
import com.RewardsManagement.rewards.dto.RewardResponse;
import com.RewardsManagement.rewards.dto.TransactionRequest;
import com.RewardsManagement.rewards.entity.Transaction;
import com.RewardsManagement.rewards.exceptionHandling.BadRequestException;
import com.RewardsManagement.rewards.exceptionHandling.ResourceNotFoundException;
import com.RewardsManagement.rewards.repository.RewardsRepository;
import com.RewardsManagement.rewards.service.RewardsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RewardsServiceTest {

	@Mock
	private RewardsRepository repository;

	@InjectMocks
	private RewardsService service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Test for create transactions

	@Test
	void testCreateTransaction_Success() {
		TransactionRequest request = new TransactionRequest("Jitendra", 120, LocalDate.now());

		Transaction saved = new Transaction();
		saved.setCustomerId("Jitendra");
		saved.setAmount(120);

		when(repository.save(any(Transaction.class))).thenReturn(saved);

		Transaction result = service.createTransaction(request);

		assertNotNull(result);
		assertEquals("Jitendra", result.getCustomerId());
		verify(repository, times(1)).save(any(Transaction.class));
	}

	@Test
	void testCreateTransaction_InvalidAmount() {
		TransactionRequest request = new TransactionRequest("Jitendra", -10, LocalDate.now());

		assertThrows(BadRequestException.class, () -> service.createTransaction(request));
	}

	@Test
	void testCreateTransaction_BlankCustomerId() {
		TransactionRequest request = new TransactionRequest("", 100, LocalDate.now());

		assertThrows(BadRequestException.class, () -> service.createTransaction(request));
	}

	@Test
	void testCreateTransaction_FutureDate() {
		TransactionRequest request = new TransactionRequest("Jitendra", 100, LocalDate.now().plusDays(1));

		assertThrows(BadRequestException.class, () -> service.createTransaction(request));
	}

	// Test for calculate rewards
	@Test
	void testCalculateRewards_Success() {
		Transaction t1 = new Transaction("Jitendra", 120.0, LocalDate.now(), 90.0);
		Transaction t2 = new Transaction("Jitendra", 80.0, LocalDate.now(), 30.0);

		when(repository.findByDateBetween(any(), any())).thenReturn(Arrays.asList(t1, t2));

		List<RewardResponse> result = service.calculateRewards();

		assertFalse(result.isEmpty());
		assertEquals("Jitendra", result.get(0).getCustomerId());
	}

	@Test
	void testCalculateRewards_EmptyList() {
		when(repository.findByDateBetween(any(), any())).thenReturn(Collections.emptyList());

		List<RewardResponse> result = service.calculateRewards();

		assertTrue(result.isEmpty());
	}

	// Test for getting custom results.
	
	@Test
	void testFindByCustomerIdAndDateBetween_Success() {
		Transaction t1 = new Transaction("Jitendra", 100.0, LocalDate.now(), 50.0);

		when(repository.findByCustomerIdAndDateBetween(any(), any(), any())).thenReturn(List.of(t1));

		CustomerTransactionResponse response = service.findByCustomerIdAndDateBetween("Jitendra",
				LocalDate.now().minusDays(5), LocalDate.now());

		assertNotNull(response);
		assertEquals("Jitendra", response.getCustomerId());
		assertEquals(1, response.getTransactions().size());
	}

	@Test
	void testFindByCustomerIdAndDateBetween_NoData() {
		when(repository.findByCustomerIdAndDateBetween(any(), any(), any())).thenReturn(Collections.emptyList());

		assertThrows(ResourceNotFoundException.class, () -> service.findByCustomerIdAndDateBetween("Jitendra",
				LocalDate.now().minusDays(5), LocalDate.now()));
	}

	@Test
	void testFindByCustomerIdAndDateBetween_InvalidInput() {
		assertThrows(BadRequestException.class,
				() -> service.findByCustomerIdAndDateBetween("", LocalDate.now(), LocalDate.now()));
	}

	@Test
	void testFindByCustomerIdAndDateBetween_InvalidDateRange() {
		assertThrows(BadRequestException.class, () -> service.findByCustomerIdAndDateBetween("Jitendra",
				LocalDate.now(), LocalDate.now().minusDays(1)));
	}
}
