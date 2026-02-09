package com.amituofo.common.ui.util;

import javax.swing.SwingWorker;

public class UIWorker<T> extends SwingWorker<T, Void> {
	private UIUpdator<T> runnable;

	private Exception e = null;

	public UIWorker(UIUpdator<T> runnable) {
		this.runnable = runnable;
	}

	@Override
	protected T doInBackground() {
		try {
			return runnable.prepareData();
		} catch (Exception e) {
			this.e = e;
		}

		return null;

	}

	@Override
	protected void done() {
		if (this.e != null) {
			try {
				runnable.exception(e);
			} finally {
				runnable.done();
			}

			return;
		}

		try {
			T data = get();
			if (data != null) {
				runnable.updateUI(data);
			}
		} catch (Exception e) {
			runnable.exception(e);
		} finally {
			runnable.done();
		}
	}
}
