package com.amituofo.common.kit.retry;

import org.apache.logging.log4j.Logger;

public interface RetryService<T> {

	void stop();

	RetryService<T> start();

	void addRetryOn(T data);

	RetryService<T> withLogger(Logger log);

	RetryService<T> withTryAction(TryAction<T> retryAction, String actionDescription);

	RetryService<T> withRetryPolicy(RetryPolicy<T> retryPolicy);

}
