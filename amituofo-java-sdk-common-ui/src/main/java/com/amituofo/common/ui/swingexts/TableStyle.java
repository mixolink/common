package com.amituofo.common.ui.swingexts;

import java.awt.Color;

public class TableStyle {
	public final static Color GRID_COLOR = new Color(240, 240, 240);
	private Color gridColor = GRID_COLOR;
	private int selectionMode = -1;// ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
	private int rowHeight = 24;
	private int autoResizeMode = -1;
	private boolean enableDefaultSorter = false;
	private Color alternateRowBackground = null;

	public TableStyle() {
	}

	public TableStyle(int rowHeight, int selectionMode, int autoResizeMode) {
		this(GRID_COLOR, rowHeight, selectionMode, autoResizeMode);
	}

	public TableStyle(Color gridColor, int rowHeight, int selectionMode, int autoResizeMode) {
		super();
		this.gridColor = gridColor;
		this.rowHeight = rowHeight;
		this.selectionMode = selectionMode;
		this.autoResizeMode = autoResizeMode;
	}

	public Color getGridColor() {
		return gridColor;
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public int getAutoResizeMode() {
		return autoResizeMode;
	}

	public boolean isEnableDefaultSorter() {
		return enableDefaultSorter;
	}

	public TableStyle setGridColor(Color gridColor) {
		this.gridColor = gridColor;
		return this;
	}

	public TableStyle setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
		return this;
	}

	public TableStyle setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
		return this;
	}

	public TableStyle setAutoResizeMode(int autoResizeMode) {
		this.autoResizeMode = autoResizeMode;
		return this;
	}

	public TableStyle setEnableDefaultSorter(boolean enableDefaultSorter) {
		this.enableDefaultSorter = enableDefaultSorter;
		return this;
	}

	public Color getAlternateRowBackground() {
		return alternateRowBackground;
	}

	public TableStyle setAlternateRowBackground(Color alternateRowBackground) {
		this.alternateRowBackground = alternateRowBackground;
		return this;
	}

}
