package com.amituofo.common.ui.action;

public abstract class SwingWorkerAction<DATA> {
	
	protected boolean isInterruptable = false;

	public SwingWorkerAction() {
		super();
	}
	public SwingWorkerAction(boolean isInterruptable) {
		super();
		this.isInterruptable = isInterruptable;
	}

	public abstract DATA doInBackground();

	public abstract void updateUI(DATA data);

	public boolean interrupt() throws InterruptedException {
		return false;
	}

	public boolean isInterruptable() {
		return isInterruptable;
	}
}
