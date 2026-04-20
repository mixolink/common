package com.amituofo.common.ui.swingexts.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class JECircularProgressBar extends JComponent {
	private int value = 0;
	private int minimum = 0;
	private int maximum = 100;

	private Color progressColor;
	private Color backgroundColor;
	private boolean showText = true;
	private float percentage = 0;

	public JECircularProgressBar() {
		setOpaque(false);
		updateColors();
//		setPreferredSize(new Dimension(100, 100));
	}

	/**
	 * 初始化/更新系统颜色
	 */
	private void updateColors() {
		// 尝试获取系统进度条颜色
		Color fg = UIManager.getColor("ProgressBar.foreground");
		Color bg = UIManager.getColor("ProgressBar.background");

		// 如果系统返回 null 或获取不到，则使用保底配色（类似标准的蓝灰配色）
		this.progressColor = (fg != null) ? fg : new Color(0, 120, 215);
		this.backgroundColor = (bg != null) ? bg : new Color(225, 225, 225);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		updateColors();
	}

	// --- 核心方法：设置当前值和最大值 ---

	public void setMinimum(int min) {
		this.minimum = min;
		repaint();
	}

	public void setMaximum(int max) {
		this.maximum = max;
		repaint();
	}

	public void plusValue(int delta) {
		setValue(this.value + delta);
	}

	public void set100Percent() {
		setValue(maximum);
	}

	public void setValue(int newvalue) {
		// 1. 限制范围
		this.value = Math.max(minimum, Math.min(maximum, newvalue));

		// 2. 计算比例 (当前值 - 最小值) / (最大值 - 最小值)
		float range = maximum - minimum;
		float newpercentage = (range <= 0) ? 0 : (float) (this.value - minimum) / range;

		// 3. (可选) 如果你想保留三位小数精度，应该对计算后的比例做处理，而不是对 value
		newpercentage = Math.round(newpercentage * 100) / 100.0f;

		// 4. 更新并重绘
		if (this.percentage != newpercentage) {
			this.percentage = newpercentage;
			repaint();
		}
	}

	public void setProgressColor(Color color) {
		this.progressColor = color;
	}

	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	/**
	 * 关键 2: 重写首选大小。 布局管理器会询问组件：“你希望多大？” 我们返回一个参考值，但实际绘制会根据 getWidth() 动态缩放。
	 */
	@Override
	public Dimension getPreferredSize() {
		// 如果父容器没限制，默认给一个和普通按钮高度相仿的大小（如 24-32）
		return new Dimension(24, 24);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(16, 16);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// 关键 3: 动态计算。不要使用固定数值，全部依赖 getWidth() 和 getHeight()
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth();
		int h = getHeight();

		// 取宽高的最小值作为直径，确保它是正圆而不是椭圆
		int size = Math.min(w, h) - 4;
		if (size <= 0)
			return; // 容错处理

		// 计算居中坐标
		int x = (w - size) / 2;
		int y = (h - size) / 2;

		// --- 开始绘制 ---
		g2.setColor(backgroundColor);
		g2.fillOval(x, y, size, size);

		if (percentage > 0) {
			g2.setColor(progressColor);
			double extent = -360.0 * percentage;
			g2.fill(new Arc2D.Double(x, y, size, size, 90, extent, Arc2D.PIE));
		}

		if (showText) {
			// 文字大小也随组件高度自动缩放
			float fontSize = Math.max(9f, size / 3.0f);
			g2.setFont(getFont().deriveFont(Font.BOLD, fontSize));
			g2.setColor(UIManager.getColor("Label.foreground"));

			String text = (int) (percentage * 100) + "%";
			FontMetrics fm = g2.getFontMetrics();
			int textX = (w - fm.stringWidth(text)) / 2;
			int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
			g2.drawString(text, textX, textY);
		}

		g2.dispose();
	}

}