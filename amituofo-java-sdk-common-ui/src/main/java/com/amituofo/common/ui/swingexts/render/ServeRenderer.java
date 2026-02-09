package com.amituofo.common.ui.swingexts.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import com.amituofo.common.type.RuntimeNames;

public class ServeRenderer extends DefaultStatusRenderer {

	public ServeRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		String status;

		if (value == null) {
			status = RuntimeNames.IDLE;
		} else {
			status = (String) value;
		}

		if (isSelected) {
			super.renderByDefault(table, status, isSelected, hasFocus, row, column);
		} else {
			if (RuntimeNames.ERROR.equals(status)) {
				setBackground(Error);
			} else if (RuntimeNames.NORMAL.equals(status) || RuntimeNames.TRANSFERING.equals(status)) {
				setBackground(Running);
			} else if (RuntimeNames.PAUSE.equals(status)) {
				setBackground(Idle);
			} else if (RuntimeNames.CLEANING.equals(status)) {
				setBackground(Color.YELLOW);
			} else {
				setBackground(Stoped);
			}

		}

		setText(value);

		return this;
	}

}