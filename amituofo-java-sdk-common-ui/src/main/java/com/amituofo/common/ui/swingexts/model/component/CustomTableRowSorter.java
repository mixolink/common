package com.amituofo.common.ui.swingexts.model.component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;

public class CustomTableRowSorter<M extends CustomTableModel> extends RowSorter<M> {
	private final M model;
	private SortKey sortKey;
	private int[] viewToModel;
	private int[] modelToView;

	private SwingWorker<Void, Void> sortworker = null;

	public CustomTableRowSorter(M model) {
		this.model = model;
		resetMapping();
	}

	// ================ 核心方法实现 ================
	@Override
	public void toggleSortOrder(int column) {
		if (column < 0 || column >= model.getColumnCount()) {
			return;
		}

		// 取消正在进行的排序
		if (sortworker != null && !sortworker.isDone()) {
			sortworker.cancel(true);
		}

		sortworker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				// 切换排序顺序：ASCENDING -> DESCENDING -> UNSORTED
				SortOrder newOrder = SortOrder.ASCENDING;
				if (sortKey != null && sortKey.getColumn() == column) {
					newOrder = sortKey.getSortOrder() == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.UNSORTED;
				}

				// 更新排序键
				sortKey = newOrder != SortOrder.UNSORTED ? new SortKey(column, newOrder) : null;

				sort();
				return null;
			}

			@Override
			protected void done() {
				if (!isCancelled()) {
					fireSortOrderChanged();
				}
			}
		};
		sortworker.execute();
	}

	@Override
	public M getModel() {
		return model;
	}

	@Override
	public int convertRowIndexToModel(int index) {
		if (viewToModel == null || index < 0 || index >= viewToModel.length) {
			return index;
		}
		return viewToModel[index];
	}

	@Override
	public int convertRowIndexToView(int index) {
		if (modelToView == null || index < 0 || index >= modelToView.length) {
			return index;
		}
		return modelToView[index];
	}

	@Override
	public void setSortKeys(List<? extends SortKey> keys) {
		this.sortKey = keys != null && !keys.isEmpty() ? keys.get(0) : null;
		sort();
		fireSortOrderChanged();
	}

	@Override
	public List<? extends SortKey> getSortKeys() {
		return sortKey != null ? Collections.singletonList(sortKey) : Collections.emptyList();
	}

	@Override
	public int getViewRowCount() {
		return viewToModel == null ? 0 : viewToModel.length;
	}

	@Override
	public int getModelRowCount() {
		return model.getRowCount();
	}

	// ================ 排序逻辑 ================

	private void sort() {
		// 初始化映射
		resetMapping();

		// 如果没有排序键，保持原始顺序
		if (viewToModel == null || sortKey == null) {
			return;
		}

		// 执行排序（直接使用int[]）
		sortIndices(viewToModel);

		// 更新反向映射
		for (int i = 0; i < viewToModel.length; i++) {
			modelToView[viewToModel[i]] = i;
		}
	}

	// 自定义int数组排序方法
	private void sortIndices(int[] indices) {
		Comparator comparator = model.getComparator(sortKey.getColumn());

		// 使用插入排序（对小数据集效率高）
		for (int i = 1; i < indices.length; i++) {
			int key = indices[i];
			int j = i - 1;

			// 将当前元素与已排序部分比较
			while (j >= 0 && compareRows(comparator, indices[j], key) > 0) {
				indices[j + 1] = indices[j];
				j--;
			}
			indices[j + 1] = key;
		}
	}

	// 直接比较两行
	private int compareRows(Comparator comparator, int row1, int row2) {
		Object a = model.getValueAt(row1, sortKey.getColumn());
		Object b = model.getValueAt(row2, sortKey.getColumn());

		if (a == null)
			return (b == null) ? 0 : -1;
		if (b == null)
			return 1;

		int result;
		if (comparator != null) {
			result = comparator.compare(a, b);
		} else {
			// 默认转为字符串比较
			result = a.toString().compareTo(b.toString());
		}

		return sortKey.getSortOrder() == SortOrder.DESCENDING ? -result : result;
	}

	// ================ 模型监听 ================

	@Override
	public void modelStructureChanged() {
		sort();
	}

	@Override
	public void rowsInserted(int firstRow, int lastRow) {
		sort();
	}

	@Override
	public void rowsDeleted(int firstRow, int lastRow) {
		sort();
	}

	@Override
	public void rowsUpdated(int firstRow, int lastRow) {
//		sort();
	}

	@Override
	public void rowsUpdated(int firstRow, int lastRow, int column) {
//		if (sortKey != null && sortKey.getColumn() == column) {
//			sort();
//		}
	}

	private void resetMapping() {
		int rowCount = model.getRowCount();
		if (rowCount == 0) {
			modelToView = viewToModel = null;
		} else {
			viewToModel = new int[rowCount];
			modelToView = new int[rowCount];
			for (int i = 0; i < rowCount; i++) {
				viewToModel[i] = i;
				modelToView[i] = i;
			}
		}
	}

	@Override
	public void allRowsChanged() {
		// TODO Auto-generated method stub

	}

}