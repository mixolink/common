package com.amituofo.common.kit.interrupt;

import com.amituofo.common.api.Interruptable;

public class Interrupters implements Interruptable {
	Interrupter[] interrupters;

	public Interrupters(Interrupter... interrupters) {
		super();
		this.interrupters = interrupters;
	}

	public long getCreateTime() {
		return interrupters[0].getCreateTime();
	}

	public boolean isInterrupted() {
		for (Interrupter interrupter : interrupters) {
			if (interrupter.isInterrupted()) {
				return true;
			}
		}
		return false;
	}

	public void markInterrupted(boolean interrupted) {
		for (Interrupter interrupter : interrupters) {
			interrupter.markInterrupted(interrupted);
		}
	}

	public void addListener(InterrupterListener listener) {
		if (listener == null) {
			return;
		}

		for (Interrupter interrupter : interrupters) {
			interrupter.addListener(listener);
		}
	}

	@Override
	public boolean interrupt() throws InterruptedException {
		for (Interrupter interrupter : interrupters) {
			interrupter.interrupt();
		}
		return true;
	}
}
