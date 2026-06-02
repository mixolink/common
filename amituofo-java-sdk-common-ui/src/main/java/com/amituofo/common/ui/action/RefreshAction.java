package com.amituofo.common.ui.action;

import com.amituofo.common.api.Refreshable;

public interface RefreshAction extends Refreshable {
	default void clearCache() {
	}

	default void pauseAutoRefresh() {
	}

	default void continueAutoRefresh() {
	}
}
