package com.RewardsManagement.rewards.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RewardsManagement.rewards.entity.Transaction;

@Repository
public interface RewardsRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByDateBetween(LocalDate start, LocalDate end);

	List<Transaction> findByCustomerIdAndDateBetween(String customerId, LocalDate startDate, LocalDate endDate);
}
