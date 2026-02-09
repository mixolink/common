package com.amituofo.common.kit.thread;

public interface ProcessControlHandler {

	public void interrupted();

	public int getProcessId();

	boolean isAlive();
}
