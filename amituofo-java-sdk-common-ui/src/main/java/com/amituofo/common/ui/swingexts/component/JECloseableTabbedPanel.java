package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;

import com.amituofo.common.api.Closeable;
import com.amituofo.common.api.Destroyable;
import com.amituofo.common.ui.action.RefreshAction;

/**
 * 标签panel应用类
 * 
 * @author sohan
 *
 */
public class JECloseableTabbedPanel extends JETabbedPanel implements RefreshAction, Destroyable {
	private boolean actived = false;
	private Font titleDefaultFont = null;
	private Color titleDefaultForeground;

	public JECloseableTabbedPanel() {
		super();
	}

	public JECloseableTabbedPanel(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	public JECloseableTabbedPanel(int tabPlacement) {
		super(tabPlacement);
	}

	public void addTab(JETabPanel component, Icon icon, boolean closeable) {
		super.addTab(component.getTitle(), icon, component);
		if (!closeable) {
			this.setCloseButtonVisibleAt(this.getTabCount() - 1, false);
		}
	}

	/**
	 * Makes the close button at the specified indes visible or invisible
	 */
	public void setCloseButtonVisibleAt(int aIndex, boolean aVisible) {
		JECloseButtonTab cbt = (JECloseButtonTab) super.getTabComponentAt(aIndex);
		cbt.closingLabel.setVisible(aVisible);
	}

	public void setTitleDefaultFont(Font font) {
		this.titleDefaultFont = font;
	}

	public void setTitleDefaultForeground(Color color) {
		titleDefaultForeground = color;
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		JECloseButtonTab tab = new JECloseButtonTab(this, component, title, icon);
		if (titleDefaultFont != null) {
			tab.setTitleFont(titleDefaultFont);
		}
		if (titleDefaultForeground != null) {
			tab.setTitleForeground(titleDefaultForeground);
		}
		setTabComponentAt(index, tab);
	}

	@Override
	public void setTitleAt(int index, String title) {
		super.setTitleAt(index, title);
		JECloseButtonTab cbt = (JECloseButtonTab) super.getTabComponentAt(index);
		if (cbt != null) {
			cbt.label.setText(title);
		}
	}

	public boolean hasTab(String title) {
		return getFirstTab(title) != null;
	}

	public JECloseButtonTab getFirstTab(String title) {
		int c = super.getTabCount();
		for (int i = 0; i < c; i++) {
			JECloseButtonTab cbt = (JECloseButtonTab) super.getTabComponentAt(i);
			if (cbt.getTitle().equals(title)) {
				return cbt;
			}
		}

		return null;
	}

	@Override
	public void setIconAt(int index, Icon icon) {
		super.setIconAt(index, icon);
		JECloseButtonTab cbt = (JECloseButtonTab) super.getTabComponentAt(index);
		cbt.label.setIcon(icon);
	}

	@Override
	public void setComponentAt(int index, Component component) {
		JECloseButtonTab cbt = (JECloseButtonTab) super.getTabComponentAt(index);
		super.setComponentAt(index, component);
		cbt.tab = component;
	}

	@Override
	public void refresh() {
		this.getActiveTabPanel().refresh();
	}

	@Override
	public synchronized void destroy() {
		int cnt = super.getTabCount();
		if (cnt > 0) {
			for (int i = 0; i < cnt; i++) {
				Component tab = super.getComponentAt(i);
				if (tab instanceof Destroyable) {
					((Destroyable) tab).destroy();
				}

				if (tab instanceof Closeable) {
					((Closeable) tab).close();
				}
			}

			super.removeAll();
		}
	}
}
