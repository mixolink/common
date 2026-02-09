package com.amituofo.common.ui.swingexts.component;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

public class JEIntegerInputField extends JFormattedTextField {

	public JEIntegerInputField() {
		this(0, Integer.MAX_VALUE, true);
	}
	
	public JEIntegerInputField(boolean comma) {
		this(0, Integer.MAX_VALUE, comma);
	}

	public JEIntegerInputField(int min, int max, boolean comma) {
		super(createFormatter(min, max, comma));
	}

	protected static AbstractFormatter createFormatter(int min, int max, boolean comma) {
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(comma);
		NumberFormatter formatter = new NumberFormatter(format);
//		formatter.setValueClass(Integer.class);
		formatter.setMinimum(min);
		formatter.setMaximum(max);
		formatter.setAllowsInvalid(false);
		// If you want the value to be committed on each keystroke instead of focus lost
		formatter.setCommitsOnValidEdit(true);

		return formatter;
	}

}
