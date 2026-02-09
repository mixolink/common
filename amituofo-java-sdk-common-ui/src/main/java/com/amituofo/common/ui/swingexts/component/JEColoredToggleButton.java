package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.plaf.metal.MetalToggleButtonUI;

public class JEColoredToggleButton extends JToggleButton {

	private Color selectedBackground;

	public JEColoredToggleButton() {
		super();
	}

	public JEColoredToggleButton(Action a) {
		super(a);
	}

	public JEColoredToggleButton(Icon icon, boolean selected) {
		super(icon, selected);
	}

	public JEColoredToggleButton(Icon icon) {
		super(icon);
	}

	public JEColoredToggleButton(String text, boolean selected) {
		super(text, selected);
	}

	public JEColoredToggleButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
	}

	public JEColoredToggleButton(String text, Icon icon) {
		super(text, icon);
	}

	public JEColoredToggleButton(String text) {
		super(text);
	}

	public void setSelectedBackground(Color color) {
		this.selectedBackground = color;
		
		setUI(new MetalToggleButtonUI() {
		    @Override
		    protected Color getSelectColor() {
		        return selectedBackground;
		    }
		});

	}
}