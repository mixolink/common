package com.amituofo.common.ui.swingexts.render;

import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JTable;
import javax.swing.border.LineBorder;

import com.amituofo.common.type.RunStatus;

public class RunStatusRenderer extends DefaultStatusRenderer {

	public RunStatusRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		RunStatus status;

		if (value == null) {
			status = RunStatus.Unknown;
		} else {
			status = (RunStatus) value;
		}

		if (isSelected) {
			super.renderByDefault(t, status, isSelected, hasFocus, row, column);
		} else {
			switch (status) {
			case Pending:
				setBackground(Pending);
				break;
			case Starting:
				setBackground(Starting);
				break;
			case Initializing:
				setBackground(Initializing);
				break;
			case Running:
				setBackground(Running);
				break;
//			case Pause:
//				setBackground(Pause);
//				break;
//			case Idle:
//				setBackground(Idle);
//				break;
//			case Restarting:
//				setBackground(Restarting);
//				break;
			case Stopping:
				setBackground(Stopping);
				break;
			case Interrupting:
				setBackground(Interrupting);
				break;
//			case Finishing:
//				setBackground(Finishing);
//				break;
			case Stoped:
				setBackground(Stoped);
				break;
//			case Interrupted:
//				setBackground(Interrupted);
//				break;
//			case Stoped:
//				setBackground(Finished);
//				break;
//			case Dead:
//			case Error:
//				setBackground(Error);
//				break;
			case Unknown:
			default:
				setBackground(Unknown);
				break;
			}
		}

//		setHorizontalAlignment(JLabel.CENTER);
		super.setText(status);

		return this;
	}

}