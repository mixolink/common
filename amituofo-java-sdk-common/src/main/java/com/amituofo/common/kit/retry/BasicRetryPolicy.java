package com.amituofo.common.kit.retry;

public abstract class BasicRetryPolicy<T> implements RetryPolicy<T> {

	private int eachTryIdelTime = 500;
	private int maxRetryTimes = -1;

	public BasicRetryPolicy() {
		super();
	}

	public BasicRetryPolicy(int eachTryIdelTime, int maxRetryTimes) {
		super();
		this.eachTryIdelTime = eachTryIdelTime;
		this.maxRetryTimes = maxRetryTimes;
	}

	@Override
	public int getEachTryIdelTime() {
		return eachTryIdelTime;
	}

	@Override
	public int getMaxRetryTimes() {
		return maxRetryTimes;
	}

	@Override
	public void setEachTryIdelTime(int retryInterval) {
		this.eachTryIdelTime = retryInterval;
	}

	@Override
	public void setMaxRetryTimes(int maxRetryTimes) {
		this.maxRetryTimes = maxRetryTimes;
	}

	@Override
	public boolean isNeedRetry(RetryContext<T> context) {
		// -1 无限重试
		if (maxRetryTimes < 0) {
			return true;
		}

		return context.retryTimes > maxRetryTimes;
	}

}
