package com.amituofo.common.ui.action;

public interface PerformAction<T1, T2> {
	void actionPerformed(T1 meta, T2 data);
}
