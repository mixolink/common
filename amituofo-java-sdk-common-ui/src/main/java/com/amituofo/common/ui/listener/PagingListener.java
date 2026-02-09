package com.amituofo.common.ui.listener;

public interface PagingListener {
	void gotoPage(int offset, int count);

	void loadingAll();

	void cancel();
}
