package com.amituofo.common.ui.define;
public enum TextFieldMenu {
	Cut("Cut"), Copy("Copy"), Paste("Paste"), SelectAll("Select All"), ClearPasteGo("Clear & Paste & Go");

	private String title;

	TextFieldMenu(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return title;
	}
}