package com.amituofo.common.ui.swingexts.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.text.Document;

public class ArcTextArea extends JTextArea {

	public ArcTextArea() {
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
	}

	public ArcTextArea(String text) {
		super(text);
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
	}

	public ArcTextArea(Document doc) {
		super(doc);
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
	}

	public ArcTextArea(int rows, int columns) {
		super(rows, columns);
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
	}

	public ArcTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
	}

	public ArcTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		setBorder(ArcBorder.DEFAULT);
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		int arc = UIManager.getInt("Component.arc");
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			// 圆角矩形填充,范围和 border 的 arc 保持一致,精确盖住整个组件区域
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		} finally {
			g2.dispose();
		}
		// setOpaque(false) 之后 super 不会再画方形背景,只会画文字内容
		super.paintComponent(g);
	}
}
