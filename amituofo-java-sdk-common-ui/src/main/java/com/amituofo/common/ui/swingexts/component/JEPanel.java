package com.amituofo.common.ui.swingexts.component;

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.amituofo.common.api.DestroyListener;
import com.amituofo.common.ui.action.DestroyAction;
import com.amituofo.common.ui.action.RefreshAction;
import com.amituofo.common.ui.util.UIUtils;

public class JEPanel extends JPanel implements RefreshAction, DestroyAction {
	private List<DestroyListener> destroyListeners = null;
	private boolean destroyed = false;

	public JEPanel() {
		super();
	}

	public JEPanel(LayoutManager layout) {
		super(layout);
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
