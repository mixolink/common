package com.amituofo.common.ui.swingexts.component;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import com.formdev.flatlaf.ui.FlatRoundBorder;

public class ArcSpinner extends JSpinner {

	public ArcSpinner() {
		putClientProperty("JComponent.roundRect", true);
//		putClientProperty("Component.arc", 4);
//		putClientProperty("FlatLaf.style", "arc: 4");
//		setBorder(new FlatRoundBorder());
//		updateUI();
	}

	public ArcSpinner(SpinnerModel model) {
		super(model);
		putClientProperty("JComponent.roundRect", true);
//		putClientProperty("Component.arc", 4);
//		putClientProperty("FlatLaf.style", "arc: 4");
//		setBorder(new FlatRoundBorder());
//		updateUI();
	}

}
