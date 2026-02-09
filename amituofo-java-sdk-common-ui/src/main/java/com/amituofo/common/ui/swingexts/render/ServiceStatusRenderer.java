package com.amituofo.common.ui.swingexts.render;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.amituofo.common.type.ServiceStatus;

public class ServiceStatusRenderer extends DefaultStatusRenderer {
	private final static Color CLR_SUCCEED = RunStatusRenderer.Running;
	private final static Color CLR_ERR = RunStatusRenderer.Error;
	private final static Color CLR_ALERT = RunStatusRenderer.Alert;
//	private final static Color CLR_SUCCEEDPARD = new Color(143, 188, 143);
//	private final static Color CLR_HANDLENOTHING = new Color(255, 196, 0);

	public ServiceStatusRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			super.renderByDefault(table, value, isSelected, hasFocus, row, column);
		} else {
			ServiceStatus status;

			if (value == null) {
				status = ServiceStatus.Unknown;
			} else {
				status = (ServiceStatus) value;
			}

			switch (status) {
			case Offline:
				setBackground(CLR_ERR);
				break;
			case Online:
				setBackground(CLR_SUCCEED);
				break;
			case Unknown:
				setBackground(CLR_ALERT);
				break;
			}
		}

		setLabel(value);

		return this;
	}

	private void setLabel(Object value) {
		super.setText(value == null ? ServiceStatus.Unknown.name() : ((ServiceStatus) value).name());
	}

}