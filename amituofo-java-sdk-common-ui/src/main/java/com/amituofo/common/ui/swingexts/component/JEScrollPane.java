package com.amituofo.common.ui.swingexts.component;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JScrollPane;

import com.amituofo.common.api.DestroyListener;
import com.amituofo.common.ui.action.DestroyAction;
import com.amituofo.common.ui.action.RefreshAction;
import com.amituofo.common.ui.listener.ActiveListener;
import com.amituofo.common.ui.util.UIUtils;

public class JEScrollPane extends JScrollPane implements RefreshAction, DestroyAction {
	private List<DestroyListener> destroyListeners = null;
	private boolean destroyed = false;

	public JEScrollPane() {
		super();
	}

	public JEScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}

	public JEScrollPane(Component view) {
		super(view);
	}

	public JEScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	@Override
	public void refresh() {
	}

	@Override
	public synchronized void destroy() {
		if (destroyed) {
			return;
		}

		if (destroyListeners != null && !destroyListeners.isEmpty()) {
			for (DestroyListener destroyListener : destroyListeners) {
				destroyListener.destroying();
			}
			destroyListeners.clear();
			destroyListeners = null;
		}

		UIUtils.removeAllComponents(this);
		destroyed = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	// @Override
	public void addDestroyListener(DestroyListener listener) {
		if (listener == null) {
			return;
		}

		if (destroyListeners == null) {
			destroyListeners = new ArrayList<>();
		}

		destroyListeners.add(listener);
	}
}
