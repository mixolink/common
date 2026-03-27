package com.amituofo.common.kit.interrupt;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.common.api.Interruptable;

public class Interrupter implements Interruptable {
	protected boolean interrupted = false;

	private List<InterrupterListener> listeners = null;
	private long createTime = System.currentTimeMillis();

	public long getCreateTime() {
		return createTime;
	}

	public boolean isInterrupted() {
		return interrupted;
	}

	public void markInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public void addListener(InterrupterListener listener) {
		if (listener == null) {
			return;
		}

		if (listeners == null) {
			listeners = new ArrayList<>();
		}

		listeners.add(listener);
	}

	@Override
	public boolean interrupt() throws InterruptedException {
		markInterrupted(true);

		if (listeners != null) {
			for (InterrupterListener interrupterListener : listeners) {
				interrupterListener.interrupted();
			}
		}

		return true;
	}

	public static Interrupter newInterrupter() {
		return new Interrupter();
	}

	public static Interrupter newInterrupter(InterrupterListener listener) {
		Interrupter interrupter = new Interrupter();
		if (listener != null) {
			interrupter.addListener(listener);
		}
		return interrupter;
	}
}
