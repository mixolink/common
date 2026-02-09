package com.amituofo.common.kit.retry;

public interface RetryPolicy<T> {

	boolean isMetRetryCondition(RetryContext<T> actionRetry);

	boolean isNeedRetry(RetryContext<T> context);

	int getEachTryIdelTime();

	void setMaxRetryTimes(int maxRetryTimes);

	void setEachTryIdelTime(int retryInterval);

	int getMaxRetryTimes();

}
