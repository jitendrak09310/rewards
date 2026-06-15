package com.RewardsManagement.rewards.dto;

import java.util.List;

import com.RewardsManagement.rewards.entity.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerTransactionResponse {

	private String customerId;
	private List<Transaction> transactions;
	private double totalPoints;
}