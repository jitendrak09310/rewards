package com.RewardsManagement.rewards.exceptionHandling;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
