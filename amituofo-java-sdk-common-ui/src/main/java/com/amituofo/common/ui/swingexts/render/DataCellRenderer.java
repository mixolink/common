package com.amituofo.common.ui.swingexts.render;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.amituofo.common.ui.lang.DataCell;

public class DataCellRenderer extends TableLabelCellRenderer {
	public DataCellRenderer() {
		setHorizontalTextPosition(SwingConstants.RIGHT);
		setVerticalTextPosition(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.LEFT);
		setVerticalAlignment(SwingConstants.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.renderByDefault(table, value, isSelected, hasFocus, row, column);

		setLabel(value);

		return this;
	}

	protected void setLabel(Object value) {
		if (value != null) {
			if (value instanceof DataCell) {
				DataCell d = (DataCell) value;

				setText(d.getTitle());
				setIcon(d.getIcon());
			} else {
				setText(value.toString());
			}
		} else {
			setText("");
			setIcon(null);
		}
	}

}