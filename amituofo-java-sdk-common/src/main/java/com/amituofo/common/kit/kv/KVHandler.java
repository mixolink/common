package com.amituofo.common.kit.kv;

import com.amituofo.common.api.DataHandler;
import com.amituofo.common.ex.HandleException;
import com.amituofo.common.type.HandleFeedback;

public interface KVHandler<T> extends DataHandler<T, String, HandleFeedback> {
	public HandleFeedback handle(int action, String key, T value) throws HandleException;

}