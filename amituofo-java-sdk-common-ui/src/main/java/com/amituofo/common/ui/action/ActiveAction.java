package com.amituofo.common.ui.action;

public interface ActiveAction {
	void deactiving();

	void activing();
	
	boolean isActived();

	void forceDeactiving();

	void forceActiving();
}
