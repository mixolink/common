package com.amituofo.common.ui.util;

public class InputConfirm {
	int selection = 0;
	boolean remember = false;

	public InputConfirm() {
	}

	public InputConfirm(int selection, boolean remember) {
		super();
		this.selection = selection;
		this.remember = remember;
	}

	public boolean isFirstSelected() {
		return selection == 0;
	}

	public int getSelection() {
		return selection;
	}

	public boolean isRemember() {
		return remember;
	}

}
