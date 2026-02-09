package com.amituofo.common.ui.swingexts.component;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import com.amituofo.common.ui.util.UIUtils;

public class JENormalLoadingPanel extends JPanel {
	JProgressBar progressBar = new JProgressBar();

	public JENormalLoadingPanel() {
		setLayout(new BorderLayout(0, 0));
//		UIManager.put("ProgressBar.selectionBackground",Color.BLUE);
//		UIManager.put("ProgressBar.selectionForeground",Color.WHITE);
//		progressBar.setOpaque(true);
//		progressBar.setForeground(UIManager.getColor("TextField.background"));
//		progressBar.setBackground(UIManager.getColor("TextField.background"));
//		progressBar.setIndeterminate(true);
		add(progressBar, BorderLayout.CENTER);
	}

	public void setIndeterminate(boolean enable) {
//		UIUtils.invokeLater(() -> {
			progressBar.setIndeterminate(enable);
//		});
	}

}