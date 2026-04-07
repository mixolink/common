package com.amituofo.common.ui.swingexts.component;

import java.awt.Component;

import javax.swing.JScrollPane;

public class ArcScrollPane extends JScrollPane {

	public ArcScrollPane() {
//		setBorder(null);
//		setViewportBorder(null);
//		setOpaque(false);
//		getViewport().setOpaque(false);
		ArcBorder.updateArcProperty(this);
	}

	public ArcScrollPane(Component view) {
		super(view);
//		setBorder(null);
//		setViewportBorder(null);
//		setOpaque(false);
//		getViewport().setOpaque(false);
		ArcBorder.updateArcProperty(this);
	}

	public ArcScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
//		setBorder(null);
//		setViewportBorder(null);
//		setOpaque(false);
//		getViewport().setOpaque(false);
		ArcBorder.updateArcProperty(this);
	}

	public ArcScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
//		setBorder(null);
//		setViewportBorder(null);
//		setOpaque(false);
//		getViewport().setOpaque(false);
		ArcBorder.updateArcProperty(this);
	}

}
