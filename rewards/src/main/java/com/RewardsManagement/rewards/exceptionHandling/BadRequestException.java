package com.RewardsManagement.rewards.exceptionHandling;

public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}