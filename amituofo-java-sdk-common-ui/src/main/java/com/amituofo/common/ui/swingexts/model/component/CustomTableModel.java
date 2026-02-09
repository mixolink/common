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
	private int[] columnWidths;
	private int[] columnMaxWidths;
	private int[] columnHAlignments;
	private TableCellRenderer[] cellRenderers;
	private TableCellEditor[] cellEditors;
	private TableStyle tableStyle;

	public CustomTableModel(Class[] columnTypes, String[] columnNames, int[] columnWidths, int[] columnHAlignments, TableCellRenderer[] cellRenderers,
			TableCellEditor[] cellEditors) {
		this(null, columnTypes, columnNames, columnWidths, new int[0], columnHAlignments, cellRenderers, cellEditors);
	}

	public CustomTableModel(TableStyle tableStyle, Class[] columnTypes, String[] columnNames, int[] columnWidths, int[] columnHAlignments,
			TableCellRenderer[] cellRenderers, TableCellEditor[] cellEditors) {
		this(tableStyle, columnTypes, columnNames, columnWidths, new int[0], columnHAlignments, cellRenderers, cellEditors);
	}

	public CustomTableModel(TableStyle tableStyle, Class[] columnTypes, String[] columnNames, int[] columnWidths,
			// int[] columnMinWidths,
			int[] columnMaxWidths, int[] columnHAlignments, TableCellRenderer[] cellRenderers, TableCellEditor[] cellEditors) {
		this.tableStyle = (tableStyle == null ? new TableStyle() : tableStyle);
		this.columnClass = columnTypes;
		this.columnNames = columnNames;
		this.columnWidths = columnWidths;
		// this.columnMinWidths = columnMinWidths;
		this.columnMaxWidths = columnMaxWidths;
		this.columnHAlignments = columnHAlignments;
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
		return columnWidths;
	}

	public int[] getColumnMaxWidths() {
		return columnMaxWidths;
	}

	public int[] getColumnHorizontalAlignments() {
		return columnHAlignments;
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
