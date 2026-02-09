package com.amituofo.common.ui.swingexts.component;

import javax.swing.*;

import com.amituofo.common.ui.util.UIUtils;
import com.amituofo.common.util.StringUtils;

import java.awt.*;

public class JEUnavailablePanel extends JPanel {
	private static final Color LINECLR = new Color(220, 20, 60); // 红色
	private String text = "";
	private Font font = UIUtils.deriveFont(Font.BOLD, 16);

	public JEUnavailablePanel() {
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		// 开启抗锯齿
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 设置虚线绘制叉子
		float[] dash = { 10f, 10f };
		g2d.setColor(LINECLR);
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, dash, 0.0f));

		int width = getWidth();
		int height = getHeight();
		int padding = 50;
		int startX = padding;
		int startY = padding;
		int endX = width - padding;
		int endY = height - padding;

		// 画叉子
		g2d.drawLine(startX, startY, endX, endY);
		g2d.drawLine(endX, startY, startX, endY);

		if (StringUtils.isNotEmpty(text)) {
			// 设置文字颜色和字体
			g2d.setColor(LINECLR);
			g2d.setFont(font);
//			Font font = UIManager.getFont("Label.font");

			// 计算文字宽高以便居中
			FontMetrics metrics = g2d.getFontMetrics(font);
			int textWidth = metrics.stringWidth(text);
			int textHeight = metrics.getHeight();
			int textX = (width - textWidth) / 2;
			int textY = (height - textHeight) / 2 + metrics.getAscent();

			// 绘制文字
			g2d.drawString(text, textX, textY);
		}
	}

	public void setText(String text) {
		this.text = text;
		this.repaint();
	}
}
