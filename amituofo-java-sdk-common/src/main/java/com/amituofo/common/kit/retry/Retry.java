package com.amituofo.common.kit.retry;

import com.amituofo.common.type.HandleFeedback;

public class Retry<DATA> {
	private TryAction<DATA> retryAction;
	private RetryContext<DATA> retryContext;
	private RetryPolicy<DATA> retryPolicy;

	public Retry(DATA conf, RetryPolicy<DATA> retryPolicy, TryAction<DATA> retryAction) {
		super();
		this.retryContext = new RetryContext<DATA>(conf);
		this.retryPolicy = retryPolicy;
		this.retryAction = retryAction;
	}

	public HandleFeedback retry() throws Exception {
		retryContext.plusRetryTimes();

		long currenttime = System.currentTimeMillis();
		try {
			HandleFeedback hf = retryAction.retry(retryContext, retryContext.data);
			return hf;
		} catch (Exception e) {
			throw e;
		} finally {
			retryContext.lastUsedTime = System.currentTimeMillis() - currenttime;
			retryContext.updateLastRetryTime();
		}

	}

	public RetryPolicy<DATA> getRetryPolicy() {
		return retryPolicy;
	}

	public RetryContext<DATA> getContext() {
		return retryContext;
	}

//	public boolean ifLastRetryIntervalMoreThan(int interval) {
//		return (System.currentTimeMillis() - retryContext.lastRetryTime) >= interval;
//	}

}
