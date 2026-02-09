package com.amituofo.common.ui.swingexts;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class Components {
	private final List<JComponent> components = new ArrayList<JComponent>();

	public Components() {
	}
	
	public void add(JComponent c) {
		components.add(c);
	}

}
