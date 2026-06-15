package com.RewardsManagement.rewards.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.RewardsManagement.rewards.dto.CustomerTransactionResponse;
import com.RewardsManagement.rewards.dto.RewardResponse;
import com.RewardsManagement.rewards.dto.TransactionRequest;
import com.RewardsManagement.rewards.entity.Transaction;
import com.RewardsManagement.rewards.service.RewardsService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class RewardsController {

	private final RewardsService service;

	@PostMapping("/createTransaction")
	public Transaction create(@Valid @RequestBody TransactionRequest request) {
		return service.createTransaction(request);
	}

	@GetMapping("/getRewards")
	public List<RewardResponse> getRewards() {
		return service.calculateRewards();
	}

	@GetMapping("/getCustomerTransactions")
	public ResponseEntity<CustomerTransactionResponse> getTransactionsByCustomer(@RequestParam String customerId,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {

		return ResponseEntity.ok(service.findByCustomerIdAndDateBetween(customerId, startDate, endDate));
	}
}