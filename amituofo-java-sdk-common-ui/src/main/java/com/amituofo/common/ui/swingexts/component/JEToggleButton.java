package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;

import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.amituofo.common.resource.IconResource;

public class JEToggleButton extends JToggleButton {

	public JEToggleButton() {
		super(IconResource.ICON_SWITCH_OFF_24x24);

		this.setBorder(new LineBorder(new Color(255, 255, 255)));
		this.setForeground(Color.WHITE);
//		this.setBackground(new Color(105, 105, 105));
		this.setRolloverSelectedIcon(IconResource.ICON_SWITCH_OFF_24x24);
		// this.setSelectedBackground(new Color(32, 178, 170));
		this.setSelectedIcon(IconResource.ICON_SWITCH_ON_24x24);

		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JEToggleButton.this.setText(JEToggleButton.this.isSelected() ? "ON " : "OFF");
			}
		});

	}

	@Override
	public void setSelected(boolean b) {
		super.setSelected(b);
		this.setText(b ? "ON " : "OFF");
	}
	
}