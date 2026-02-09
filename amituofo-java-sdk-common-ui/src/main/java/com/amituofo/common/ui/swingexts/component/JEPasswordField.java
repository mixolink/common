package com.amituofo.common.ui.swingexts.component;

import javax.swing.JPasswordField;
import javax.swing.text.Document;

public class JEPasswordField extends JPasswordField {
	private boolean isModified = false;

	public JEPasswordField() {
		registEvent();
	}

	public JEPasswordField(String text) {
		super(text);
		registEvent();
	}

	public JEPasswordField(int columns) {
		super(columns);
		registEvent();
	}

	public JEPasswordField(String text, int columns) {
		super(text, columns);
		registEvent();
	}

	public JEPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
		registEvent();
	}

	private void registEvent() {
		getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				isModified = true;
				// 可以在这里做其他实时检查，比如长度、强度等
			}

			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				isModified = true;
			}

			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				// 一般纯文本组件不会触发这个，基本可以忽略
			}
		});
	}

	public boolean isPasswordModified() {
		return isModified;
	}

	public void reset() {
		isModified = false;
		this.setText("");
	}
}
