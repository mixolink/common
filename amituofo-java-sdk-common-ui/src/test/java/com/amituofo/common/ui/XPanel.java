package com.amituofo.common.ui;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class XPanel extends JPanel {
	public XPanel() {
		setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel = new JPanel();
		add(panel);
		
		JPanel panel_1 = new JPanel();
		add(panel_1);
	}

}
