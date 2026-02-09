package com.amituofo.common.kit.retry;

public class ReturnableValue<T> {
	private T value;
	private int tryTimes = 0;
	private long lastTryTime = 0;
	private boolean needRetry = false;

//	public ReturnableValue() {
//		super();
//	}

	public ReturnableValue(T value) {
		super();
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public int getTryTimes() {
		return tryTimes;
	}

	public long getLastTryTime() {
		return lastTryTime;
	}

	public void plusTryTimes() {
		tryTimes++;
		lastTryTime = System.currentTimeMillis();
	}

	public boolean reachTryLimit(int maxRetryTimes) {
		return tryTimes >= maxRetryTimes;
	}

	public int remainTryTimes(int maxRetryTimes) {
		return maxRetryTimes - tryTimes;
	}

	public boolean isNeedRetry() {
		return needRetry;
	}

	public void setNeedRetry(boolean needRetry) {
		this.needRetry = needRetry;
	}

	public boolean isTryIntervalGreaterThan(long interval) {
		return (System.currentTimeMillis() - lastTryTime) > interval;
	}

	public boolean isTryIntervalSmallerThan(long interval) {
		return (System.currentTimeMillis() - lastTryTime) < interval;
	}
}
