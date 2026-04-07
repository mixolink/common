package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.formdev.flatlaf.ui.FlatLineBorder;

public class ArcTitledBorder extends TitledBorder {

	public ArcTitledBorder(String title) {
		super(new ArcBorder(), title, TitledBorder.LEADING, TitledBorder.TOP, null, null);
	}

}
