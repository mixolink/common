package com.amituofo.common.ui.swingexts.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

// 导入必要的类
import javax.swing.ImageIcon;
import javax.swing.JPanel;

// 创建一个新的类来处理背景图片的绘制
public class JEImagePanel extends JPanel {

	private Image img;

	public JEImagePanel(URL imageUrl) {
		this(imageUrl, false);
	}

	public JEImagePanel(URL imageUrl, boolean rotate180) {
		// 加载图片
		ImageIcon imageIcon = new ImageIcon(imageUrl);
		// 建议设置不透明性为 false，以便内容也能看到背景，但由于 JPanel 默认是 true，
		// 且重写 paintComponent 已经处理了背景绘制，可以忽略。
		// setOpaque(false);
		img = imageIcon.getImage();
		if (rotate180) {
			img = flipImage180Degrees(img);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // 保持原有的绘制逻辑（如清空背景色）

		if (img != null) {
			// 绘制图片，拉伸或缩放到面板的整个大小
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

	private BufferedImage flipImage180Degrees(Image originalImage) {
		if (originalImage == null) {
			return null;
		}

		int width = originalImage.getWidth(this);
		int height = originalImage.getHeight(this);

		if (width <= 0 || height <= 0) {
			System.err.println("Invalid image dimensions.");
			return null;
		}

		// 创建一个新的 BufferedImage，用于绘制翻转后的图片
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// 设置高质量渲染提示 (可选)
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// --- 核心变换 ---
		// 1. 移动到图片中心点
		// 2. 旋转180度 (PI 弧度)
		// 3. 再移回来 (因为旋转是以坐标原点(0,0)为中心的，所以需要先平移到中心，旋转后平移回来)

		// 或者更简单的方式：
		// 围绕图片的中心点进行旋转
		// 先平移到图片的中心 (width/2, height/2)
		g2d.translate(width / 2, height / 2);
		// 旋转180度 (Math.PI 弧度)
		g2d.rotate(Math.PI);
		// 再次平移，但这次是负的，将图片移回到画布的正确位置，
		// 因为旋转后图片的左上角会变成右下角。
		g2d.translate(-width / 2, -height / 2);

		// 绘制原始图片到变换后的 Graphics2D 上
		g2d.drawImage(originalImage, 0, 0, null);
		g2d.dispose(); // 释放 Graphics2D 资源

		return bufferedImage;
	}
}