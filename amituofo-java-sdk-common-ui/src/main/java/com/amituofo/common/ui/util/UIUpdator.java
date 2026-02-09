package com.amituofo.common.ui.util;

public interface UIUpdator<T> {
	T prepareData() throws Exception;

	void updateUI(T data);

	void exception(Exception e);
	
	void done();
}