package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;

import com.amituofo.common.ui.util.UIUtils;

public class JEDescription extends JTextArea {
	public JEDescription() {
		this(null);
	}

	public JEDescription(String text) {
		super(text);
//		setForeground(new Color(192, 192, 192));
		setFont(UIUtils.deriveFontSizePlus(-1));
		setLineWrap(true);
		setEditable(false);
		setEnabled(false);
		setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(211, 211, 211)));
		setWrapStyleWord(true);
	}

}
