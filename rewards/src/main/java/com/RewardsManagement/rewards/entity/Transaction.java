package com.RewardsManagement.rewards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String customerId;

	@Column(nullable = false)
	private double amount;

	@Column(nullable = false)
	private LocalDate date;
	
	@Column(nullable = false)
	private double pointsEarned;
}
