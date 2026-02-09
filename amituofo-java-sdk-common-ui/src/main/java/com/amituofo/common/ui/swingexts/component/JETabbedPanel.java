package com.amituofo.common.ui.swingexts.component;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.amituofo.common.ui.action.CardAction;
import com.amituofo.common.ui.action.LazyInitializeAction;

public class JETabbedPanel extends JTabbedPane implements LazyInitializeAction {
	protected CardAction activedPanel = null;
	private boolean tabSelectionListenerEnabled = false;

	public JETabbedPanel() {
		super();
	}

	public JETabbedPanel(int tabPlacement) {
		super(tabPlacement);
	}

	public JETabbedPanel(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	public int addTab(JETabPanel component, Icon icon) {
		super.addTab(component.getTitle(), icon, component);
		return this.getTabCount() - 1;
	}

	public int insertTab(JETabPanel component, Icon icon, int index) {
		super.insertTab(component.getTitle(), icon, component, "", index);
		return this.getTabCount() - 1;
	}

	@Override
	public void enableTabSelectionListener() {
		if (tabSelectionListenerEnabled) {
			return;
		}

		tabSelectionListenerEnabled = true;
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				final Component c = ((JTabbedPane) e.getSource()).getSelectedComponent();
				switchToTab(c);
			}
		});
	}

	public JETabPanel switchTab(String title) {
		Component[] cs = this.getComponents();
		for (Component component : cs) {
			if (component instanceof JETabPanel) {
				if (title.equalsIgnoreCase(((JETabPanel) component).getTitle())) {
					this.setSelectedComponent(component);
					return ((JETabPanel) component);
				}
			}
		}

		return null;
	}

	public boolean hasTab(String title) {
		Component[] cs = this.getComponents();
		for (Component component : cs) {
			if (component instanceof JETabPanel) {
				if (title.equalsIgnoreCase(((JETabPanel) component).getTitle())) {
					return true;
				}
			}
		}

		return false;
	}

	public JETabPanel getActiveTabPanel() {
		if (this.getTabCount() == 0) {
			return null;
		}

		activedPanel = (CardAction) this.getSelectedComponent();

		if (activedPanel instanceof JEScrollTabPane) {
			return ((JEScrollTabPane) activedPanel).getPanel();
		}

		return (JETabPanel) activedPanel;
	}

//	@Override
//	public void setSelectedIndex(int index) {
//		super.setSelectedIndex(index);
//		Component c = super.getSelectedComponent();
//		switchToTab(c);
//	}

	private synchronized void switchToTab(Component c) {
		if (c == activedPanel) {
			return;
		}

		if (!(c instanceof CardAction)) {
			return;
		}

		if (activedPanel != null && activedPanel.isActived()) {
			activedPanel.deactiving();
		}

		CardAction tab = (CardAction) c;
		if (!tab.isActived()) {
			tab.activing();
		}

		activedPanel = (CardAction) tab;
	}

}
