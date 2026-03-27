package com.amituofo.common.ui.action;

import com.amituofo.common.api.InterruptableRunnable;

public abstract class SwingWorkerDelegateAction extends SwingWorkerAction<Void> {

	protected boolean isInterruptable = false;
	private Runnable runnable = null;

	public SwingWorkerDelegateAction(Runnable runnable) {
		super();
		this.runnable = runnable;
		this.isInterruptable = runnable instanceof InterruptableRunnable;
	}

	public Void doInBackground() {
		runnable.run();
		return null;
	}

	public abstract void updateUI(Void data);

	public boolean interrupt() throws InterruptedException {
		if (isInterruptable) {
			((InterruptableRunnable) runnable).markInterrupted(true);
			return true;
		}

		return false;
	}

	public boolean isInterruptable() {
		return isInterruptable;
	}
}
