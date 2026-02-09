package com.amituofo.common.ui.action;

public interface RefreshAction {
	void refresh();

//	void clearCache();
	default void clearCache() {
	}

	default void pauseAutoRefresh() {
	}

	default void continueAutoRefresh() {
	}
}
