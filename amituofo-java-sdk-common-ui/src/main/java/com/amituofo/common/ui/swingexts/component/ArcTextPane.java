package com.amituofo.common.ui.swingexts.component;

import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class ArcTextPane extends JTextPane {

	public ArcTextPane() {
		setBorder(ArcBorder.DEFAULT);
	}

	public ArcTextPane(StyledDocument doc) {
		super(doc);
		setBorder(ArcBorder.DEFAULT);
	}

}
