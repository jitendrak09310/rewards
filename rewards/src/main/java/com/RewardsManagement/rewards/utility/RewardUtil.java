package com.RewardsManagement.rewards.utility;

public class RewardUtil {

	public static int calculatePoints(double amount) {

		if (amount <= 50)
			return 0;

		if (amount <= 100) {
			return (int) (amount - 50);
		}

		return (int) ((100 - 50) + (amount - 100) * 2);
	}
}