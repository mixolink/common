package com.amituofo.common.kit.retry;

public class RetryContext<DATA> {
	protected long lastRetryTime;
	protected long lastUsedTime;
	protected int retryTimes;
	protected DATA data;

	public RetryContext(DATA data) {
		super();
		this.data = data;
	}

	public long getLastUsedTime() {
		return lastUsedTime;
	}

	public long getLastRetryTime() {
		return lastRetryTime;
	}

	public void updateLastRetryTime() {
		this.lastRetryTime = System.currentTimeMillis();
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void plusRetryTimes() {
		this.retryTimes++;
	}

	public DATA getData() {
		return data;
	}

	public void setData(DATA data) {
		this.data = data;
	}

//	public boolean ifLastRetryIntervalMoreThan(int interval) {
//		return (System.currentTimeMillis() - lastRetryTime) >= interval;
//	}

}
