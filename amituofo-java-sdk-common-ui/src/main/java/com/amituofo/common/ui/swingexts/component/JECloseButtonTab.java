package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.amituofo.common.api.Closeable;
import com.amituofo.common.api.Destroyable;
import com.amituofo.common.ui.resource.Icons;

public class JECloseButtonTab extends JPanel {
	protected Component tab;
	protected JLabel label;
	protected JLabel closingLabel;

	public JECloseButtonTab(final JTabbedPane parent, Component aTab, String aTitle, Icon aIcon) {
		tab = aTab;

//		Icon CLOSING_ICON = new ImageIcon(CloseButtonTab.class.getResource("/com/amituofo/common/ui/resource/image/close-1-16x16.png"));
//		Icon CLOSING_ICON_SELECTED = new ImageIcon(CloseButtonTab.class.getResource("/com/amituofo/common/ui/resource/image/close-2-16x16.png"));
		setOpaque(false);
		setLayout(new GridBagLayout());
		setVisible(true);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 2, 5);

		label = new JLabel(aTitle);
		label.setIcon(aIcon);
		add(label, gbc);
		closingLabel = new JLabel(Icons.CLOSING_ICON);
		closingLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) getParent().getParent();
				int tabIndex = parent.indexOfComponent(tab);

				if (tab instanceof JECloseableTabPanel && ((JECloseableTabPanel) tab).tabClosing(tabIndex, tabbedPane.getTabCount())) {
					tabbedPane.removeTabAt(tabIndex);
//					Component c = tabbedPane.getComponentAt(tabIndex);

					if (tab instanceof Destroyable) {
						((Destroyable) tab).destroy();
					}

					if (tab instanceof Closeable) {
						((Closeable) tab).close();
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (Icons.CLOSING_ICON_SELECTED != null) {
					closingLabel.setIcon(Icons.CLOSING_ICON_SELECTED);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (Icons.CLOSING_ICON_SELECTED != null) {
					closingLabel.setIcon(Icons.CLOSING_ICON);
				}
			}
		});
		gbc.insets = new Insets(0, 0, 0, 0);
		add(closingLabel, gbc);
	}

	public Component getComponent() {
		return tab;
	}

	public String getTitle() {
		return label.getText();
	}

	public void setTitleFont(Font font) {
		label.setFont(font);
	}

	public void setTitleForeground(Color color) {
		label.setForeground(color);
	}

}