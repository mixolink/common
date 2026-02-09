
package com.amituofo.common.ui.swingexts.render;

import java.awt.Component;

import javax.swing.JTable;

public class DefaultTableLabelCellRenderer extends TableLabelCellRenderer {

	public DefaultTableLabelCellRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.renderByDefault(table, value, isSelected, hasFocus, row, column);

		setText(value);

		return this;
	}

}
