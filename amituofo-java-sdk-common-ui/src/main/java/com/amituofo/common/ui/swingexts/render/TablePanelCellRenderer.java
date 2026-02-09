
package com.amituofo.common.ui.swingexts.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public abstract class TablePanelCellRenderer extends JPanel implements TableCellRenderer {

	protected Color unselectedForeground;
	protected Color unselectedBackground;

	public TablePanelCellRenderer() {
		super();
		this.setOpaque(false);

		unselectedForeground = (Color) UIManager.get("Table.foreground");
		unselectedBackground = (Color) UIManager.get("Table.background");
//		selectedForeground = (Color) UIManager.get("Table.selectionForeground");
//		selectedBackground = (Color) UIManager.get("Table.selectionBackground");
	}

	protected void renderByDefault(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			super.setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			super.setForeground(unselectedForeground);
			super.setBackground(unselectedBackground);
		}
	}

	protected void setSelectionGroundColor(JTable table, JComponent[] cs) {
		for (JComponent c : cs) {
			c.setForeground(table.getSelectionForeground());
			c.setBackground(table.getSelectionBackground());
		}
	}

	protected void setUnselectedGroundColor(JTable table, JComponent[] cs) {
		for (JComponent c : cs) {
			c.setForeground(unselectedForeground);
			c.setBackground(unselectedBackground);
		}
	}

}
