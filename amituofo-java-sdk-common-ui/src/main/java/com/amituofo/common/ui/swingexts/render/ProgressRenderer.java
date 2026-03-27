package com.amituofo.common.ui.swingexts.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

public class ProgressRenderer extends JProgressBar implements TableCellRenderer {
	public ProgressRenderer(int min, int max) {
		super(min, max);
		this.setStringPainted(true);
//		this.setForeground(new Color(60, 179, 113));
		this.setBorder(new LineBorder(new Color(230, 230, 250)));
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		int progress = ((Integer) value).intValue();

//		if (progress == -811212) {
//			this.setStringPainted(true);
//			this.setIndeterminate(true);
//		} else {
			this.setValue(progress);
//		}
		return this;
	}
}