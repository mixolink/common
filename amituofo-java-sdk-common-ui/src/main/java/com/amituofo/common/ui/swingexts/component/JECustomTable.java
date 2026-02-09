package com.amituofo.common.ui.swingexts.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.amituofo.common.ui.listener.RightClickListener;
import com.amituofo.common.ui.swingexts.TableStyle;
import com.amituofo.common.ui.swingexts.model.Record;
import com.amituofo.common.ui.swingexts.model.component.CustomTableModel;
import com.amituofo.common.ui.swingexts.model.component.CustomTableRowSorter;
import com.amituofo.common.ui.swingexts.render.DefaultTableLabelCellRenderer;
import com.amituofo.common.ui.swingexts.render.TableLabelCellRenderer;

public class JECustomTable<T extends CustomTableModel<ITEM>, ITEM> extends JTable {
	private static final Color NORMAL_BK_CLR = UIManager.getColor("Table.background");

	private List<ITEM> lastSelectedRows = null;
//	private List<ModelIndexData<ITEM>> lastSelectedRowsWithModelIndex = null;
	private Color alternateRowBackground = null;

	public JECustomTable() {
		this.setAutoCreateRowSorter(false);
	}

	public JECustomTable(T dm) {
		super(dm);
		this.setAutoCreateRowSorter(false);

		updateStyle(dm);
	}

	public void setModel(T dataModel) {
		T dm = (T) dataModel;
		super.setModel(dm);

		updateStyle(dm);
	}

	public void updateStyle(T dm) {
		TableStyle tableStyle = dm.getTableStyle();
		if (tableStyle != null) {
			if (tableStyle.getGridColor() != null) {
				this.setGridColor(tableStyle.getGridColor());
			}
			if (tableStyle.getAutoResizeMode() >= 0) {
				this.setAutoResizeMode(tableStyle.getAutoResizeMode());
			}
			if (tableStyle.getSelectionMode() >= 0) {
				this.setSelectionMode(tableStyle.getSelectionMode());
			}
			if (tableStyle.getRowHeight() > 0) {
				this.setRowHeight(tableStyle.getRowHeight());
			}
			if (tableStyle.isEnableDefaultSorter()) {
				this.enableDefaultSorter();
			} else {
				this.disableDefaultSorter();
			}
		} else {
			this.disableDefaultSorter();
		}

		this.alternateRowBackground = tableStyle.getAlternateRowBackground();

//		int count;
//		TableColumnModel columnModel = this.getColumnModel();// 获取列模型
//
//		int[] columnWidth = dm.getColumnWidths();
//		if (columnWidth != null) {
//			count = Math.min(columnModel.getColumnCount(), columnWidth.length);// 获取列数量
//			for (int i = 0; i < count; i++) {// 遍历列
//				TableColumn column = columnModel.getColumn(i);// 获取列对象
//				if (columnWidth[i] > 0) {
//					column.setWidth(columnWidth[i]);// 以数组元素设置列的宽度
//					column.setPreferredWidth(columnWidth[i]);// 以数组元素设置列的宽度
//					// column.setWidth(columnWidth[i]);
//				} else if (columnWidth[i] == 0) {
//					column.setMinWidth(0);
//					column.setMaxWidth(0);
//					column.setPreferredWidth(0);
//					column.setWidth(0);
//				}
//			}
//		}
//
//		// ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer())
//		// .setHorizontalAlignment(JLabel.CENTER);
//
//		int[] columnMaxWidth = dm.getColumnMaxWidths();
//		if (columnMaxWidth != null) {
//			count = Math.min(columnModel.getColumnCount(), columnMaxWidth.length);// 获取列数量
//			for (int i = 0; i < count; i++) {// 遍历列
//				TableColumn column = columnModel.getColumn(i);// 获取列对象
//				if (columnMaxWidth[i] > 0) {
//					column.setMaxWidth(columnMaxWidth[i]);// 以数组元素设置列的宽度
//				}
//			}
//		}
//
//		TableCellRenderer[] cellRenderers = dm.getCellRenderers();
//		if (cellRenderers != null) {
//			count = this.getColumnCount();// 获取列数量
//			for (int i = 0; i < count; i++) {// 遍历列
//				TableCellRenderer render = null;
//				if (i < cellRenderers.length && cellRenderers[i] != null) {
//					render = cellRenderers[i];// 设置
//				}
//
//				if (render == null) {
////					render = new DefaultTableCellRenderer();// 设置
//					continue;
//				}
//
//				TableColumn column = columnModel.getColumn(i);// 获取列对象
//				column.setCellRenderer(render);
//			}
//		}
//
//		int[] columnHorizontalAlignments = dm.getColumnHorizontalAlignments();
//		if (columnHorizontalAlignments != null) {
//			count = Math.min(columnModel.getColumnCount(), columnHorizontalAlignments.length);// 获取列数量
//			for (int i = 0; i < count; i++) {// 遍历列
//				if (columnHorizontalAlignments[i] > -1) {
//					TableColumn column = columnModel.getColumn(i);// 获取列对象
//					TableCellRenderer render = column.getCellRenderer();
//					if (render != null && render instanceof DefaultTableCellRenderer) {
//						((DefaultTableCellRenderer) render).setHorizontalAlignment(columnHorizontalAlignments[i]);// 居中对齐
//					}
//				}
//			}
//		}
//
//		TableCellEditor[] cellEditor = dm.getCellEditors();
//		if (cellEditor != null) {
//			count = Math.min(this.getColumnCount(), cellEditor.length);// 获取列数量
//			for (int i = 0; i < count; i++) {// 遍历列
//				if (cellEditor[i] != null) {
//					TableCellEditor editor = cellEditor[i];// 设置
//					if (editor != null) {
//						TableColumn column = columnModel.getColumn(i);// 获取列对象
//						column.setCellEditor(editor);
//					}
//				}
//			}
//		}
	}

	public CustomTableRowSorter getRowSorter() {
		return (CustomTableRowSorter) super.getRowSorter();
	}

	public void updateColumnStyle(int i, TableColumn column) {
		CustomTableModel dm = ((CustomTableModel) getModel());

//		TableStyle tableStyle = dm.getTableStyle();
//		if (tableStyle != null) {
//			if (tableStyle.getGridColor() != null) {
//				this.setGridColor(tableStyle.getGridColor());
//			}
//			if (tableStyle.getAutoResizeMode() >= 0) {
//				this.setAutoResizeMode(tableStyle.getAutoResizeMode());
//			}
//			if (tableStyle.getSelectionMode() >= 0) {
//				this.setSelectionMode(tableStyle.getSelectionMode());
//			}
//			if (tableStyle.getRowHeight() > 0) {
//				this.setRowHeight(tableStyle.getRowHeight());
//			}
//			if (tableStyle.isEnableDefaultSorter()) {
//				this.enableDefaultSorter();
//			} else {
//				this.disableDefaultSorter();
//			}
//		} else {
//			this.disableDefaultSorter();
//		}

		int[] columnWidth = dm.getColumnWidths();
		if (columnWidth != null && i < columnWidth.length) {
			if (columnWidth[i] > 0) {
				column.setWidth(columnWidth[i]);// 以数组元素设置列的宽度
				column.setPreferredWidth(columnWidth[i]);// 以数组元素设置列的宽度
				// column.setWidth(columnWidth[i]);
			} else if (columnWidth[i] == 0) {
				column.setMinWidth(0);
				column.setMaxWidth(0);
				column.setPreferredWidth(0);
				column.setWidth(0);
			}
		}

		// ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer())
		// .setHorizontalAlignment(JLabel.CENTER);

		int[] columnMaxWidth = dm.getColumnMaxWidths();
		if (columnMaxWidth != null && i < columnMaxWidth.length) {
			if (columnMaxWidth[i] > 0) {
				column.setMaxWidth(columnMaxWidth[i]);// 以数组元素设置列的宽度
			}
		}

		TableCellRenderer[] cellRenderers = dm.getCellRenderers();
		if (cellRenderers != null && i < cellRenderers.length) {
			TableCellRenderer render = cellRenderers[i];// 设置
			if (render != null) {
				column.setCellRenderer(render);
			}
		}

		int[] columnHorizontalAlignments = dm.getColumnHorizontalAlignments();
		if (columnHorizontalAlignments != null && i < columnHorizontalAlignments.length) {
			if (columnHorizontalAlignments[i] > -1) {
				TableCellRenderer render = column.getCellRenderer();
				if (render == null) {
					//DefaultTableCellRenderer
					render = new DefaultTableLabelCellRenderer();// 设置
					column.setCellRenderer(render);
					((TableLabelCellRenderer) render).setHorizontalAlignment(columnHorizontalAlignments[i]);// 居中对齐
				} else if (render instanceof TableLabelCellRenderer) {
					((TableLabelCellRenderer) render).setHorizontalAlignment(columnHorizontalAlignments[i]);// 居中对齐
				}
			}
		}

		TableCellEditor[] cellEditor = dm.getCellEditors();
		if (cellEditor != null && i < cellEditor.length) {
			if (cellEditor[i] != null) {
				TableCellEditor editor = cellEditor[i];// 设置
				if (editor != null) {
					column.setCellEditor(editor);
				}
			}
		}
	}

	@Override
	public void createDefaultColumnsFromModel() {
		TableModel m = getModel();
		if (m != null) {
			// Remove any current columns
			TableColumnModel cm = getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}

			// Create new columns from the data model info
			for (int i = 0; i < m.getColumnCount(); i++) {
				TableColumn newColumn = new TableColumn(i);
				addColumn(newColumn);
				updateColumnStyle(i, newColumn);
			}
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		TableCellEditor[] cellEditor = ((CustomTableModel) this.getModel()).getCellEditors();
		if (cellEditor != null && cellEditor.length > 0) {
			if (column < cellEditor.length) {
				return cellEditor[column] != null;
			}
		}

		return false;
	}

	public void setColumnVisable(int columnIndex, boolean visable) {
		TableColumnModel columnModel = this.getColumnModel();// 获取列模型
		int count = columnModel.getColumnCount();

		if (columnIndex >= count) {
			return;
		}

		TableColumn column = columnModel.getColumn(columnIndex);// 获取列对象

		if (visable) {
			int[] columnWidths = ((T) this.getModel()).getColumnWidths();
			if (columnIndex < columnWidths.length) {
				column.setWidth(columnWidths[columnIndex]);// 以数组元素设置列的宽度
				// column.setMinWidth(columnWidths[columnIndex]);
				column.setMaxWidth(columnWidths[columnIndex]);
				column.setPreferredWidth(columnWidths[columnIndex]);// 以数组元素设置列的宽度
			}
		} else {
			column.setWidth(0);
			column.setMinWidth(0);
			column.setMaxWidth(0);
			column.setPreferredWidth(0);
		}
	}

	private void enableDefaultSorter() {
		T dm = (T) super.getModel();

		CustomTableRowSorter<T> sorter = new CustomTableRowSorter<T>(dm);
//		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dm);
//		for (int column = 0; column < dm.getColumnCount(); column++) {
//			Comparator comparator = dm.getComparator(column);
//			if (comparator != null) {
//				sorter.setComparator(column, comparator);
//			}
//		}
		this.setRowSorter(sorter);
	}

	private void disableDefaultSorter() {
		this.setRowSorter(null);
	}

	@Override
	public void addRowSelectionInterval(int index0, int index1) {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.addRowSelectionInterval(index0, index1);
	}

	@Override
	public void removeRowSelectionInterval(int index0, int index1) {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.removeRowSelectionInterval(index0, index1);
	}

	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
	}

	@Override
	public void removeAll() {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.removeAll();
	}

	@Override
	public void selectAll() {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.selectAll();
	}

	@Override
	public void clearSelection() {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.clearSelection();
	}

	@Override
	public boolean getRowSelectionAllowed() {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		return super.getRowSelectionAllowed();
	}

	@Override
	public void setRowSelectionInterval(int index0, int index1) {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		super.setRowSelectionInterval(index0, index1);
	}

	public int[] getConvertedSelectedRows() {
		int[] rows = super.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			rows[i] = convertRowIndexToModel(rows[i]);
		}

		return rows;
	}

	public boolean isAllSelected() {
		int selectedRowCount = super.getSelectedRowCount();
		int rowCount = super.getRowCount();
		return rowCount > 0 && selectedRowCount == rowCount;
	}

	public void setRightClickListener(RightClickListener<ITEM> rightClickListener) {
		if (rightClickListener != null) {
			createClickEventHandler(rightClickListener);
		}
	}

	private void createClickEventHandler(RightClickListener<ITEM> rightClickListener) {
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					// System.out.println(aa);
					int r = JECustomTable.this.rowAtPoint(e.getPoint());
					if (r >= 0 && r < JECustomTable.this.getRowCount()) {
						boolean isIncludeInCurrentSelection = false;
						int[] selectedRows = JECustomTable.this.getSelectedRows();
						for (int i : selectedRows) {
							if (r == i) {
								isIncludeInCurrentSelection = true;
								break;
							}
						}

						if (isIncludeInCurrentSelection) {
							JECustomTable.this.addRowSelectionInterval(r, r);
						} else {
							JECustomTable.this.setRowSelectionInterval(r, r);
						}

					} else {
						JECustomTable.this.clearSelection();
					}

					if (rightClickListener != null) {
						List<ITEM> selectedItems = JECustomTable.this.getSelectedItemList();
						rightClickListener.rightClicked(e, selectedItems);
					}
				}
			}
		});
	}

	public void invertSelection() {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
		int[] selectedIndexs = this.getSelectedRows();
		this.selectAll();

		for (int prevSel : selectedIndexs) {
			this.removeRowSelectionInterval(prevSel, prevSel);
		}
	}

	public void clearSelectionCache() {
		lastSelectedRows = null;
//		lastSelectedRowsWithModelIndex = null;
	}

	public List<Record<ITEM>> getSelectedRowList() {
		int[] rows = getConvertedSelectedRows();

		T model = (T) getModel();
		return model.getRecordList(rows);
	}

	public List<ITEM> getSelectedItemList() {
		if (lastSelectedRows != null) {
			return lastSelectedRows;
		}

		List<ITEM> selrows;
		T model = (T) getModel();
		if (isAllSelected()) {
			selrows = model.getAllItemList();
		} else {
			int[] rows = getConvertedSelectedRows();
			selrows = model.getItemList(rows);
		}

		if (selrows != null && selrows.size() > 0) {
			lastSelectedRows = selrows;
		}

		return selrows;
	}

//	public List<ModelIndexData<ITEM>> getSelectedItemListWithModelIndex() {
//		if (lastSelectedRowsWithModelIndex != null) {
//			return lastSelectedRowsWithModelIndex;
//		}
//
//		List<ModelIndexData<ITEM>>  selrows;
//		T model = (T) getModel();
//		if (isAllSelected()) {
//			selrows = model.getAllItemList();
//		} else {
//			int[] rows = getConvertedSelectedRows();
//			selrows = model.getItemList(rows);
//		}
//
//		if (selrows != null && selrows.size() > 0) {
//			lastSelectedRowsWithModelIndex = selrows;
//		}
//
//		return selrows;
//	}

	public ITEM getSelectedItem() {
		int[] rows = getConvertedSelectedRows();
		if (rows == null || rows.length == 0) {
			return null;
		}

		T model = (T) getModel();
		try {
			return model.getItem(rows[0]);
//			List<ITEM> items = model.getDataList(rows);
//			if (items.size() > 0) {
//				return items.get(0);
//			} else {
//				removeRowSelectionInterval(rows[0], rows[rows.length - 1]);
//			}
		} catch (Throwable e) {
			// do nothing
			try {
				removeRowSelectionInterval(rows[0], rows[rows.length - 1]);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		// 奇偶行着色
		if (alternateRowBackground == null || isRowSelected(row)) {
			return c;
		} else {
			// 2. 偶数行（row % 2 == 0）→ 保持默认背景（也不干预）
			if (row % 2 == 0) {
				// 恢复默认非选中背景色（通常来自 UIManager）
				c.setBackground(NORMAL_BK_CLR);
				return c;
			} else {
				c.setBackground(alternateRowBackground);
				return c;
			}
		}
	}
}
