package com.amituofo.common.ui.util;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * 霓虹玻璃风格图标生成器 - 智能尺寸适配版
 */
public class FileIconUtils {

	private static Color computeColor(String text) {
		int hash = text.hashCode();
		float hue = Math.abs(hash % 360) / 360f;
		// 霓虹色：保持 0.6 的饱和度和 0.95 的明度，既鲜艳又通透
		return Color.getHSBColor(hue, 0.6f, 0.95f);
	}

	public static BufferedImage renderToNeonGlassIconImage(String ext, int width, int height) {
//		if (width <= 0 || height <= 0)
//			return null;

		// 判定尺寸模式
		boolean isTiny = (width <= 18 || height <= 18);

		int len = ext.length();
		if (len > 4) {
			ext = ext.substring(0, 4);
		}
		ext = ext.toUpperCase();
		
		// 提取首字母用于小图标
		String displayChars = len > 0 ? (isTiny ? ext.substring(0, 1) : ext) : "";
		
		Color neonColor = computeColor(ext);

		// --- 1. 超采样 2倍 绘图 (Retina 级清晰度) ---
		int scale = 2;
		BufferedImage highResImg = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = highResImg.createGraphics();
		g2d.scale(scale, scale);

		// 渲染质量设置
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		// --- 2. 动态圆角 (16x16 时圆角设为 3, 48x48 时设为 10) ---
		double arc = Math.min(height * 0.25, 10.0);
		RoundRectangle2D body = new RoundRectangle2D.Double(1, 1, width - 2, height - 2, arc, arc);

		// 玻璃背景渐变
		g2d.setPaint(new LinearGradientPaint(0, 0, 0, height, new float[] { 0f, 0.4f, 1f },
				new Color[] { new Color(255, 255, 255, 255), new Color(252, 253, 255, 240), new Color(245, 247, 250, 215) }));
		g2d.fill(body);

		// 边框：稍微加深一点点以配合深色文字
		g2d.setColor(new Color(190, 200, 210, 140));
		g2d.setStroke(new BasicStroke(0.7f));
		g2d.draw(body);

		// --- 3. 底部霓虹条 ---
		// 小图标模式下霓虹条稍微加宽，增加辨识度
		int barH = isTiny ? Math.max(3, height / 5) : Math.max(3, height / 18);
		int barPadding = isTiny ? 2 : width / 4;

		// 使用裁剪确保霓虹条符合圆角
		Shape oldClip = g2d.getClip();
		g2d.setClip(body);
		g2d.setColor(neonColor);
		if (isTiny) {
			// 小图标模式：色块贴底
			g2d.fillRect(0, height - barH, width, barH);
		} else {
			// 大图标模式：悬浮胶囊
			RoundRectangle2D bar = new RoundRectangle2D.Double(barPadding, height - barH - 8, width - barPadding * 2, barH, barH, barH);
			g2d.fill(bar);
		}
		g2d.setClip(oldClip);

		// --- 4. 文字绘制 ---
		// 颜色：深碳灰色，更有质感且清晰
		g2d.setColor(new Color(51, 60, 68));

		if (isTiny) {
			// 小图标：首字母大写，加粗，居中
			g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int) (height * 0.6)));
			FontMetrics fm = g2d.getFontMetrics();
			int tx = (width - fm.stringWidth(displayChars)) / 2;
			int ty = (height - barH - fm.getHeight()) / 2 + fm.getAscent() + 1;
			g2d.drawString(displayChars, tx, ty);
		} else {
			// 大图标：全称，居中
			g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int) (height * 0.22)));
			FontMetrics fm = g2d.getFontMetrics();
			int tx = (width - fm.stringWidth(displayChars)) / 2;
			int ty = (height - barH - 10 - fm.getHeight()) / 2 + fm.getAscent() + 4;
			g2d.drawString(displayChars, tx, ty);
		}

		g2d.dispose();

		// --- 5. 平滑缩回 (解决模糊的关键) ---
		BufferedImage finalImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D finalG2d = finalImg.createGraphics();
		finalG2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		finalG2d.drawImage(highResImg, 0, 0, width, height, null);
		finalG2d.dispose();

		return finalImg;
	}
}