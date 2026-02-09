package com.amituofo.common.ui.resource;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Icons {
	private static final String BASE_PATH = "/com/amituofo/common/ui/resource/image/";

//
	private static ImageIcon getIcon(String id) {
		return new ImageIcon(Icons.class.getResource(BASE_PATH + id));
	}

//	
//	public static final ImageIcon ICON_SWITCH_ON_24x24 = getIcon("switch-on-24x24.png");
//	public static final ImageIcon ICON_SWITCH_OFF_24x24 = getIcon("switch-off-24x24.png");
//	public static final ImageIcon ICON_SWITCH_ON_32x32 = getIcon("switch-on-32x32.png");
//	public static final ImageIcon ICON_SWITCH_OFF_32x32 = getIcon("switch-off-32x32.png");
	public final static ImageIcon CLOSING_ICON = getIcon("close-1-16x16.png");
	public final static ImageIcon CLOSING_ICON_SELECTED = getIcon("close-2-16x16.png");

	public final static Icon ARROW_ICON = createArrowIcon();

	private static Icon createArrowIcon() {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				int arrowSize = 3;
				// 修改这两行：让图标在按钮中居中显示
				int centerX = x + getIconWidth() / 2;
				int centerY = y + getIconHeight() / 2;

				int[] xPoints = { centerX - arrowSize, centerX + arrowSize, centerX };
				int[] yPoints = { centerY - arrowSize / 2, centerY - arrowSize / 2, centerY + arrowSize / 2 + 1 };

				g2.setColor(c.isEnabled() ? Color.BLACK : Color.GRAY);
				g2.fillPolygon(xPoints, yPoints, 3);

				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				// 可以适当减小图标宽度以适应更窄的按钮
				return 8;
			}

			@Override
			public int getIconHeight() {
				return 12;
			}
		};
	}

}
