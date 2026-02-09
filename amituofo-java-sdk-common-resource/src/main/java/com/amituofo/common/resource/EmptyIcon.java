package com.amituofo.common.resource;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class EmptyIcon implements Icon {
	    private final int w, h;

		public EmptyIcon(int w, int h) { this.w = w; this.h = h; }
	    public void paintIcon(Component c, Graphics g, int x, int y) {}
	    public int getIconWidth()  { return w; }
	    public int getIconHeight() { return h; }
	}