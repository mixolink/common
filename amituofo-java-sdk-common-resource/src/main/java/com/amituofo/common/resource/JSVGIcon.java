package com.amituofo.common.resource;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox; // 确保导入这个类
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JSVGIcon implements Icon {
	private final SVGDocument document;
	private final int width;
	private final int height;

	public JSVGIcon(SVGDocument document, int width, int height) {
		this.document = document;
		this.width = width;
		this.height = height;
	}

	public ImageIcon getImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		paintIcon(null, g2d, 0, 0);
		g2d.dispose();
		return new ImageIcon(image);
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();

		// 1. 开启抗锯齿，确保 4K 屏渲染质量
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2d.setColor(Color.white);
		// 2. 移动到绘制位置
		g2d.translate(x, y);

		// 4. 调用你定义的那个 render 方法
		document.render(c, g2d,  new ViewBox(0, 0, width, height));

		g2d.dispose();
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}
}
