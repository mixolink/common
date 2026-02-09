package com.amituofo.common.ui.swingexts.render;

import com.amituofo.common.ui.lang.DataCell;

public class TitleCellRenderer extends DataCellRenderer {

	public TitleCellRenderer() {
	}

	@Override
	protected void setLabel(Object value) {
		if (value != null) {
			if (value instanceof DataCell) {
				setValueAndStyle((DataCell) value);
			} else {
				super.setText(value);
			}
		} else {
			setText("");
		}
	}

	protected void setValueAndStyle(DataCell value) {
		setIcon(value.getIcon());
		setText(value.getTitle());
	}

}
