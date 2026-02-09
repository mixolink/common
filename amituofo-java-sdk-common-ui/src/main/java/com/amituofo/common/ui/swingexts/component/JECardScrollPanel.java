package com.amituofo.common.ui.swingexts.component;

import java.awt.Component;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.amituofo.common.ui.action.CardAction;
import com.amituofo.common.ui.listener.ActiveListener;

public abstract class JECardScrollPanel extends JEScrollPane implements CardAction {
	private AtomicBoolean actived = new AtomicBoolean(false);
	private List<ActiveListener> activeListeners = null;

	public JECardScrollPanel() {
		super();
	}

	public JECardScrollPanel(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
	}

	public JECardScrollPanel(Component view) {
		super(view);
	}

	public JECardScrollPanel(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
	}

	@Override
	public void addActiveListener(ActiveListener listener) {
		if (listener == null) {
			return;
		}

		if (activeListeners == null) {
			activeListeners = new ArrayList<>();
		}

		activeListeners.add(listener);
	}

	@Override
	public synchronized void destroy() {
		activeListeners = null;
		super.destroy();
	}

	@Override
	public void forceActiving() {
		_Activing_();
	}

	@Override
	public void forceDeactiving() {
		_Deactiving_();
	}

	@Override
	public void deactiving() {
		if (isDestroyed()) {
			return;
		}

		synchronized (actived) {
			if (actived.get() == false) {
				return;
			}
			actived.set(false);
			_Deactiving_();

			if (activeListeners != null) {
				for (ActiveListener activeListener : activeListeners) {
					try {
						activeListener.deactived();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected abstract void _Deactiving_();

	@Override
	public void activing() {
		if (isDestroyed()) {
			return;
		}

		synchronized (actived) {
			if (actived.get()) {
				return;
			}
			actived.set(true);
			_Activing_();

			if (activeListeners != null) {
				for (ActiveListener activeListener : activeListeners) {
					try {
						activeListener.actived();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected abstract void _Activing_();

	@Override
	public boolean isActived() {
		return actived.get();
	}
}
