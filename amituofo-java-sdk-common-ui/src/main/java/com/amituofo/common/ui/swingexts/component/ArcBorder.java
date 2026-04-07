package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.UIManager;

import com.formdev.flatlaf.ui.FlatLineBorder;

public class ArcBorder extends FlatLineBorder {
	public final static ArcBorder DEFAULT = new ArcBorder();

	public final static String DEFAULT_ARC_PROPERTY = "arc: " + UIManager.getInt("Component.arc") + "; " + // 圆角半径
			"borderWidth: 1; " + // 边框粗细
//			"borderColor: "+UIManager.getColor("Component.borderColor")+"; " + // 边框颜色 #D0D0D0
			"background: $Table.background"; // 背景色随表格走

	public ArcBorder(int insets, Color lineColor, float lineThickness, int arc) {
		super(new Insets(insets, insets, insets, insets), lineColor, lineThickness, UIManager.getInt("Component.arc"));
	}

	public ArcBorder() {
		this(5);
	}

	public ArcBorder(int insets) {
		this(insets, 1);
	}

	public ArcBorder(int insets, float lineThickness) {
		this(insets, UIManager.getColor("Component.borderColor"), lineThickness, UIManager.getInt("Component.arc"));
	}

	public ArcBorder(int insets, Color lineColor, float lineThickness) {
		this(insets, lineColor, lineThickness, UIManager.getInt("Component.arc"));
	}

//	public ArcBorder(Color lineColor, float lineThickness) {
//		super(DEFAULT_INSETS, lineColor, lineThickness, UIManager.getInt("Component.arc"));
//	}

	public static void updateArcProperty(JComponent c) {
		c.putClientProperty("FlatLaf.style", DEFAULT_ARC_PROPERTY);

//		c.putClientProperty("FlatLaf.style", "arc: 12; " + // 圆角半径
//				"borderWidth: 1; " + // 边框粗细
//				"borderColor: #D0D0D0; " + // 边框颜色
//				"background: $Table.background" // 背景色随表格走
//		);
	}
}
