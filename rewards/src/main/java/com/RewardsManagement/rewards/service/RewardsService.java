package com.RewardsManagement.rewards.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.RewardsManagement.rewards.dto.RewardResponse;
import com.RewardsManagement.rewards.dto.TransactionRequest;
import com.RewardsManagement.rewards.entity.Transaction;
import com.RewardsManagement.rewards.repository.RewardsRepository;
import com.RewardsManagement.rewards.utility.RewardUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RewardsService {

	private final RewardsRepository repository;

	public Transaction createTransaction(TransactionRequest request) {

		Transaction transaction = new Transaction();
		transaction.setCustomerId(request.getCustomerId());
		transaction.setAmount(request.getAmount());
		transaction.setDate(request.getDate());
		transaction.setPointsEarned(RewardUtil.calculatePoints(request.getAmount()));
		return repository.save(transaction);
	}

	public List<RewardResponse> calculateRewards() {

		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusMonths(3);

		// fetching data for last three months.
		List<Transaction> transactions = repository.findByDateBetween(startDate, endDate);

//		collecting data month wise
		Map<String, Map<Month, Integer>> grouped = transactions.stream().collect(
				Collectors.groupingBy(Transaction::getCustomerId, Collectors.groupingBy(t -> t.getDate().getMonth(),
						Collectors.summingInt(t -> RewardUtil.calculatePoints(t.getAmount())))));

		List<RewardResponse> responses = new ArrayList<>();
//iterating for month-wise data and getting total
		for (String customerId : grouped.keySet()) {

			Map<Month, Integer> monthly = grouped.get(customerId);

			Map<String, Integer> monthlyFormatted = monthly.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));

			int total = monthly.values().stream().mapToInt(Integer::intValue).sum();

			responses.add(new RewardResponse(customerId, monthlyFormatted, total));
		}

		return responses;
	}
}