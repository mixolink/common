
package com.amituofo.common.ui.swingexts.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public abstract class TableLabelCellRenderer extends JLabel implements TableCellRenderer {

	protected Color unselectedForeground;
	protected Color unselectedBackground;

	public TableLabelCellRenderer() {
		super();
		setOpaque(true);

		unselectedForeground = (Color) UIManager.get("Table.foreground");
		unselectedBackground = (Color) UIManager.get("Table.background");
//		selectedForeground = (Color) UIManager.get("Table.selectionForeground");
//		selectedBackground = (Color) UIManager.get("Table.selectionBackground");
	}

	public void renderByDefault(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			super.setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			super.setForeground(unselectedForeground);
			super.setBackground(unselectedBackground);
		}
	}

	protected void setText(Object value) {
		if (value != null) {
			super.setText(value.toString());
		} else {
			super.setText("");
		}
	}
}
