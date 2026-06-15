package com.RewardsManagement.rewards.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.RewardsManagement.rewards.dto.CustomerTransactionResponse;
import com.RewardsManagement.rewards.dto.RewardResponse;
import com.RewardsManagement.rewards.dto.TransactionRequest;
import com.RewardsManagement.rewards.entity.Transaction;
import com.RewardsManagement.rewards.exceptionHandling.BadRequestException;
import com.RewardsManagement.rewards.exceptionHandling.ResourceNotFoundException;
import com.RewardsManagement.rewards.repository.RewardsRepository;
import com.RewardsManagement.rewards.utility.RewardUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class RewardsService {

	private final RewardsRepository repository;

	public Transaction createTransaction(TransactionRequest request) {

		try {
			if (request.getAmount() <= 0) {
				throw new BadRequestException("Amount Can't be less than zero");
			}

			if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
				throw new BadRequestException("Customer ID is required");
			}

			if (request.getDate() != null && request.getDate().isAfter(LocalDate.now())) {
				throw new BadRequestException("Transaction date cannot be in the future");
			}

			Transaction transaction = new Transaction();
			transaction.setCustomerId(request.getCustomerId());
			transaction.setAmount(request.getAmount());
			transaction.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
			transaction.setPointsEarned(RewardUtil.calculatePoints(request.getAmount()));

			return repository.save(transaction);

		} catch (BadRequestException e) {
			System.err.println("Validation Error: " + e.getMessage());
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error occurred while creating transaction", e);
		}
	}

	public List<RewardResponse> calculateRewards() {

		try {
			LocalDate endDate = LocalDate.now();
			LocalDate startDate = endDate.minusMonths(3);

			// fetching data for last three months.
			List<Transaction> transactions = repository.findByDateBetween(startDate, endDate);

			Map<String, Map<Month, Integer>> grouped = transactions.stream().collect(
					Collectors.groupingBy(Transaction::getCustomerId, Collectors.groupingBy(t -> t.getDate().getMonth(),
							Collectors.summingInt(t -> RewardUtil.calculatePoints(t.getAmount())))));

			List<RewardResponse> responses = new ArrayList<>();

			for (String customerId : grouped.keySet()) {

				Map<Month, Integer> monthly = grouped.get(customerId);

				Map<String, Integer> monthlyFormatted = monthly.entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));

				int total = monthly.values().stream().mapToInt(Integer::intValue).sum();

				responses.add(new RewardResponse(customerId, monthlyFormatted, total));
			}

			return responses;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error occurred while calculating rewards", e);
		}
	}

	public CustomerTransactionResponse findByCustomerIdAndDateBetween(String customerId, LocalDate startDate,
			LocalDate endDate) {

		try {
			if (customerId == null || customerId.isBlank()) {
				throw new BadRequestException("Customer ID is required");
			}

			if (startDate == null || endDate == null) {
				throw new BadRequestException("Start date and end date are required");
			}

			if (endDate.isBefore(startDate)) {
				throw new BadRequestException("End date cannot be before start date");
			}

			List<Transaction> transactions = repository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);

			if (transactions.isEmpty()) {
				throw new ResourceNotFoundException("No transactions found for given criteria");
			}

			// Calculating total points
			double totalPoints = transactions.stream().collect(Collectors.summingDouble(Transaction::getPointsEarned));

			return new CustomerTransactionResponse(customerId, transactions, totalPoints);

		} catch (BadRequestException | ResourceNotFoundException e) {
			System.err.println("Handled Exception: " + e.getMessage());
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Something went wrong while fetching transactions", e);
		}
	}
}