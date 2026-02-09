package com.amituofo.common.api;

public interface Service {
	public int start(Object... args);

	public void stop(int code);
}
