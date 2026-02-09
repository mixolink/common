package com.amituofo.common.ui.swingexts.component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class JEImageComponent extends JComponent {
		private final BufferedImage image;

		public JEImageComponent(BufferedImage image) {
			this.image = image;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image != null) {
				// 将BufferedImage绘制到组件的当前边界内
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			}
		}
	}