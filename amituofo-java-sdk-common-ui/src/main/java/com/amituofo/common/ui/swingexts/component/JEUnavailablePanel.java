package com.amituofo.common.ui.swingexts.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import com.amituofo.common.ui.util.UIUtils;
import com.amituofo.common.util.StringUtils;

/**
 * 无法预览/不可用面板 - Finder 风格优化版
 */
public class JEUnavailablePanel extends JPanel {
	// 使用中性灰色，显得专业且不突兀
	private static final Color COLOR_TEXT = new Color(140, 140, 140);
	private static final Color COLOR_DASH = new Color(210, 210, 210);
	private static final Color COLOR_ICON_BG = new Color(248, 248, 248);
	
	private String text = "No Preview Available";
	private Font font = UIUtils.deriveFont(Font.PLAIN, 13);

	public JEUnavailablePanel() {
		// 设置透明背景，以便更好地融入 Mixolink 的不同主题背景
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		// 开启抗锯齿，保证线条平滑（macOS 上非常重要）
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();

		// 1. 绘制外部虚线圆角框 (代替叉子)
		float[] dash = { 8f, 8f };
		g2d.setColor(COLOR_DASH);
		g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, 0));
		// 留出一定的边距
		int margin = 20;
		g2d.drawRoundRect(margin, margin, width - margin * 2, height - margin * 2, 12, 12);

		// 2. 绘制中心的文件图标剪影
		int iconW = 44;
		int iconH = 56;
		int iconX = (width - iconW) / 2;
		int iconY = (height - iconH) / 2 - 15; // 稍微上移，为下方文字留位

		// 绘制文件主体
		g2d.setColor(COLOR_ICON_BG);
		g2d.fillRoundRect(iconX, iconY, iconW, iconH, 5, 5);
		g2d.setColor(COLOR_DASH);
		g2d.setStroke(new BasicStroke(1.0f));
		g2d.drawRoundRect(iconX, iconY, iconW, iconH, 5, 5);
		
		// 绘制文件折角效果 (Finder 经典特征)
		int corner = 14;
		g2d.setColor(new Color(230, 230, 230));
		g2d.fillPolygon(
			new int[]{iconX + iconW - corner, iconX + iconW, iconX + iconW - corner}, 
			new int[]{iconY, iconY, iconY + corner}, 3
		);

		// 3. 绘制说明文字
		if (StringUtils.isNotEmpty(text)) {
			g2d.setColor(COLOR_TEXT);
			g2d.setFont(font);
			
			FontMetrics metrics = g2d.getFontMetrics(font);
			int textX = (width - metrics.stringWidth(text)) / 2;
			// 文字放在图标下方 25 像素处
			int textY = iconY + iconH + 30; 
			
			g2d.drawString(text, textX, textY);
		}

		g2d.dispose();
	}

	public void setText(String text) {
		this.text = text;
		this.repaint();
	}
}