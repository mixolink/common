package com.amituofo.common.ui.action;

import com.amituofo.common.api.DestroyListener;
import com.amituofo.common.api.Destroyable;

public interface DestroyAction extends Destroyable {

//	void destroy();

	void addDestroyListener(DestroyListener listener);

}