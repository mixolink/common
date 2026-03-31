package com.amituofo.common.ui.swingexts.model.component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.Destroyable;
import com.amituofo.common.api.ObjectHandler;
import com.amituofo.common.type.HandleFeedback;
import com.amituofo.common.ui.swingexts.TableStyle;
import com.amituofo.common.ui.swingexts.model.ItemComponentModel;
import com.amituofo.common.ui.swingexts.model.Record;
import com.amituofo.common.ui.swingexts.model.ItemModelDataInjector;

public abstract class CustomTableModel<ITEM> extends TableRecordModel<ITEM> implements Destroyable { // AbstractTableModel

	private Class[] columnClass;
	private String[] columnNames;
	private int[] COLUMNS_WIDTH;
	private int[] columnMaxWidths;
	private int[] COLUMNS_ALIGNMENTS;
	private TableCellRenderer[] cellRenderers;
	private TableCellEditor[] cellEditors;
	private TableStyle tableStyle;

	public CustomTableModel(Class[] COLUMNS_TYPE, String[] columnNames, int[] COLUMNS_WIDTH, int[] COLUMNS_ALIGNMENTS, TableCellRenderer[] cellRenderers,
			TableCellEditor[] cellEditors) {
		this(null, COLUMNS_TYPE, columnNames, COLUMNS_WIDTH, new int[0], COLUMNS_ALIGNMENTS, cellRenderers, cellEditors);
	}

	public CustomTableModel(TableStyle tableStyle, Class[] COLUMNS_TYPE, String[] columnNames, int[] COLUMNS_WIDTH, int[] COLUMNS_ALIGNMENTS,
			TableCellRenderer[] cellRenderers, TableCellEditor[] cellEditors) {
		this(tableStyle, COLUMNS_TYPE, columnNames, COLUMNS_WIDTH, new int[0], COLUMNS_ALIGNMENTS, cellRenderers, cellEditors);
	}

	public CustomTableModel(TableStyle tableStyle, Class[] COLUMNS_TYPE, String[] columnNames, int[] COLUMNS_WIDTH,
			// int[] columnMinWidths,
			int[] columnMaxWidths, int[] COLUMNS_ALIGNMENTS, TableCellRenderer[] cellRenderers, TableCellEditor[] cellEditors) {
		this.tableStyle = (tableStyle == null ? new TableStyle() : tableStyle);
		this.columnClass = COLUMNS_TYPE;
		this.columnNames = columnNames;
		this.COLUMNS_WIDTH = COLUMNS_WIDTH;
		// this.columnMinWidths = columnMinWidths;
		this.columnMaxWidths = columnMaxWidths;
		this.COLUMNS_ALIGNMENTS = COLUMNS_ALIGNMENTS;
		this.cellRenderers = cellRenderers;
		this.cellEditors = cellEditors;
	}

//	protected TableModelHelper<ITEM> buildModelHelper() {
//		return new TableModelHelper<ITEM>(this);
//	}

	public abstract Comparator getComparator(int column);

//	public abstract void destroy();

	public TableStyle getTableStyle() {
		return tableStyle;
	}

	public TableCellRenderer[] getCellRenderers() {
		if (cellRenderers == null) {
			return new TableCellRenderer[0];
		}
		return cellRenderers;
	}

	public TableCellEditor[] getCellEditors() {
		return cellEditors;
	}

	public int[] getColumnWidths() {
		return COLUMNS_WIDTH;
	}

	public int[] getColumnMaxWidths() {
		return columnMaxWidths;
	}

	public int[] getColumnHorizontalAlignments() {
		return COLUMNS_ALIGNMENTS;
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;// columnEditables[column];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	
}
