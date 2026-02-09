package com.amituofo.common.kit.retry;

public class RetryIntervalPolicy<T> extends BasicRetryPolicy<T> {

	private int minRetryInterval;

	public RetryIntervalPolicy(int interval) {
		super(0, -1);
		this.minRetryInterval = interval;
	}

	@Override
	public boolean isMetRetryCondition(RetryContext<T> context) {
		return (System.currentTimeMillis() - context.getLastRetryTime()) >= minRetryInterval;
	}

}
