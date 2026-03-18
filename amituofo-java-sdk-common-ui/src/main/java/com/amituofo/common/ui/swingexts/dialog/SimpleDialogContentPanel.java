package com.amituofo.common.ui.swingexts.dialog;

import javax.swing.JComponent;

import com.amituofo.common.ui.action.IconAction;
import com.amituofo.common.ui.action.OKCancelAction;
import com.amituofo.common.ui.action.TitleAction;
import com.amituofo.common.ui.swingexts.component.JEPanel;

public abstract class SimpleDialogContentPanel extends JEPanel implements OKCancelAction, TitleAction, IconAction {

	private SimpleDialog simpleDialog;

	public void setParentDialog(SimpleDialog simpleDialog) {
		this.simpleDialog = simpleDialog;
	}

	public void dispose() {
//		if (simpleDialog != null) {
//			simpleDialog.dispose();
//			simpleDialog = null;
//		}
		simpleDialog = null;
	}

	@Override
	public synchronized void destroy() {
		simpleDialog = null;
		super.destroy();
	}

	public SimpleDialog getParentDialog() {
		return simpleDialog;
	}

	public JComponent[] getAdditionComponents() {
		return null;
	}

	public void showing() {

	}
}
