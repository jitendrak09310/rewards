package com.RewardsManagement.rewards.dto;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

	@NotBlank
	private String customerId;

	@Min(0)
	private double amount;

	@NotNull
	private LocalDate date;

}