package com.amituofo.common.api;

public interface ExpiredListener<T> {
	void expired(T data);
}
