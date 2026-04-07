package com.amituofo.common.ui.swingexts.component;

import javax.swing.JTextArea;
import javax.swing.text.Document;

public class ArcTextArea extends JTextArea {

	public ArcTextArea() {
		setBorder(ArcBorder.DEFAULT);
	}

	public ArcTextArea(String text) {
		super(text);
		setBorder(ArcBorder.DEFAULT);
	}

	public ArcTextArea(Document doc) {
		super(doc);
		setBorder(ArcBorder.DEFAULT);
	}

	public ArcTextArea(int rows, int columns) {
		super(rows, columns);
		setBorder(ArcBorder.DEFAULT);
	}

	public ArcTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		setBorder(ArcBorder.DEFAULT);
	}

	public ArcTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		setBorder(ArcBorder.DEFAULT);
	}

}
