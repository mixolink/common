package com.amituofo.common.kit.retry;

import com.amituofo.common.ex.InitializeException;
import com.amituofo.common.kit.retry.RetryHelper.TryBody;

public abstract class ReturnableTry<T> {
	protected int retryTime = 1;
	protected TryBody trybody;

	public ReturnValue<T> run() {
		return execute(retryTime++, System.currentTimeMillis());
	}

	protected abstract ReturnValue<T> execute(int retryTimes, long time);

	protected void init() throws InitializeException {

	}

	protected void release() {

	}
	
	protected void resetInterval(long retryInterval) {
		trybody.setRetryInterval(retryInterval);
	}
}
