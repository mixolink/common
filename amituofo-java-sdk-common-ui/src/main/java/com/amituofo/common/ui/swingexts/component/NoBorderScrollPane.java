package com.amituofo.common.ui.swingexts.component;

import java.awt.Component;

import javax.swing.JScrollPane;

public class NoBorderScrollPane extends JScrollPane {

	public NoBorderScrollPane() {
		setBorder(null);
		setViewportBorder(null);
	}

	public NoBorderScrollPane(Component view) {
		super(view);
		setBorder(null);
		setViewportBorder(null);
	}

	public NoBorderScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		setBorder(null);
		setViewportBorder(null);
	}

	public NoBorderScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		setBorder(null);
		setViewportBorder(null);
	}

}
