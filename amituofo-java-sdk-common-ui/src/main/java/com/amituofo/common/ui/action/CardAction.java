package com.amituofo.common.ui.action;

import com.amituofo.common.api.Destroyable;
import com.amituofo.common.ui.listener.ActiveListener;

public interface CardAction extends ActiveAction, RefreshAction, Destroyable {
	void addActiveListener(ActiveListener listener);
}
