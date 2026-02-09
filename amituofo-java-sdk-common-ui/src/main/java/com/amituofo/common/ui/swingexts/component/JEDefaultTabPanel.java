package com.amituofo.common.ui.swingexts.component;

import java.awt.BorderLayout;

import javax.swing.JComponent;

import com.amituofo.common.ui.action.ActiveAction;
import com.amituofo.common.ui.action.RefreshAction;

public class JEDefaultTabPanel<T extends JComponent> extends JETabPanel {

	private String title;
	private T component;
	private ActiveAction activeAction = null;
	private RefreshAction refreshAction = null;

	public JEDefaultTabPanel(T component) {
		this(null, component);
	}

	public JEDefaultTabPanel(String title, T component) {
		this.title = title;
		this.component = component;
		if (component instanceof ActiveAction) {
			this.activeAction = (ActiveAction) component;
		}
		if (component instanceof RefreshAction) {
			this.refreshAction = (RefreshAction) component;
		}

		setLayout(new BorderLayout(0, 0));

		add(component, BorderLayout.CENTER);
	}

	public T getTabComponent() {
		return component;
	}

	@Override
	public void refresh() {
		if (refreshAction != null) {
			refreshAction.refresh();
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	protected void _Deactiving_() {
		if (activeAction != null) {
			activeAction.deactiving();
		}
	}

	@Override
	protected void _Activing_() {
		if (activeAction != null) {
			activeAction.activing();
		}
	}

}
