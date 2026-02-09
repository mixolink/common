package com.amituofo.common.ui.swingexts.component;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.amituofo.common.resource.IconResource;

public class JELoadingPanel extends JPanel {
	// public final static LoadingPanel INSTANCE = new LoadingPanel();

	private Image image;

	public JELoadingPanel(Image image) {
		// image = Toolkit.getDefaultToolkit().getImage(WaitingDialog.class.getResource(Constants.BASE_PATH_ICONS+"loading7.gif"));
		this.image = image;// GlobalIcon.UI16x16.of(GlobalIconNames.IMAGE_LOADING;
	}

	public JELoadingPanel() {
		this(IconResource.IMAGE_LOADING);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			// g.drawImage(image, -30, -30, this);
			g.drawImage(image, 0, 0, this);
		}
	}
}