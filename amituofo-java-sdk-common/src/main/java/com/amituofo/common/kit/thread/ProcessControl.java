package com.amituofo.common.kit.thread;

public class ProcessControl {
	private ProcessControlHandler handler = null;

	public void interrupted() {
		if (handler != null) {
			handler.interrupted();
		}
	}

	public int getProcessId() {
		if (handler != null) {
			return handler.getProcessId();
		}

		return -1;
	}

	public boolean isAlive() {
		if (handler != null) {
			return handler.isAlive();
		}

		return false;
	}

	public void setHandler(ProcessControlHandler handler) {
		this.handler = handler;
	}

}
