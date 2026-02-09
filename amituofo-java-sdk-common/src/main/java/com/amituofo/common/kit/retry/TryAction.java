package com.amituofo.common.kit.retry;

import com.amituofo.common.type.HandleFeedback;

public interface TryAction<DATA> {
//	String getName();

	HandleFeedback retry(RetryContext<DATA> retryContext, DATA data);

}
