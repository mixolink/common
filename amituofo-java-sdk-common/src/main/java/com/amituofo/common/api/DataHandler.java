package com.amituofo.common.api;

import com.amituofo.common.ex.HandleException;

public interface DataHandler<T, P, R> {
	public R handle(int action, P param, T data) throws HandleException;
}
