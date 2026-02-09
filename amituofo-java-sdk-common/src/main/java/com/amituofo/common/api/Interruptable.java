package com.amituofo.common.api;

public interface Interruptable {
	public boolean isInterrupted();

	public boolean interrupt() throws InterruptedException;
}
