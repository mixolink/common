package com.amituofo.common.ui.swingexts.component;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JEPortField extends JTextField {
	private int minPort = 1;
	private int maxPort = 65535;

	public JEPortField() {
		this(1, 65535);
	}

	public JEPortField(int defaultPort) {
		this(1, 65535);
		this.setText("" + defaultPort);
	}

	public JEPortField(int min, int max) {
		this.minPort = min;
		this.maxPort = max;
		this.setColumns(5); // 端口号最多5位

		// 1. 限制只能输入数字（实时过滤）
		((AbstractDocument) this.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (string.matches("\\d+"))
					super.insertString(fb, offset, string, attr);
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (text.matches("\\d+"))
					super.replace(fb, offset, length, text, attrs);
			}
		});

		// 2. 失去焦点时校验上下限
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePort();
			}
		});
	}

	private void validatePort() {
		String text = getText();
		if (text.isEmpty()) {
			setText(String.valueOf(minPort));
			return;
		}

		try {
			int value = Integer.parseInt(text);
			if (value < minPort)
				setText(String.valueOf(minPort));
			else if (value > maxPort)
				setText(String.valueOf(maxPort));
		} catch (NumberFormatException e) {
			setText(String.valueOf(minPort));
		}
	}

	public int getPort() {
		return Integer.parseInt(getText());
	}

	public void setPort(int port) {
		setText(String.valueOf(port));
	}
}