package com.RewardsManagement.rewards.exceptionHandling;

public class SomethingWentWrong extends RuntimeException {

	public SomethingWentWrong(String message) {
		super(message);
	}
}