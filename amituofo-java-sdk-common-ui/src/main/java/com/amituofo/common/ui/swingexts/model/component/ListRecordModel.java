package com.amituofo.common.ui.swingexts.model.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import com.amituofo.common.api.Callback;
import com.amituofo.common.ui.swingexts.model.ItemComponentModel;
import com.amituofo.common.ui.swingexts.model.ItemRecordModel;
import com.amituofo.common.ui.swingexts.model.ItemRecordConverter;
import com.amituofo.common.ui.swingexts.model.Record;
import com.amituofo.common.ui.swingexts.model.RecordModelChangedListener;
import com.amituofo.common.util.StringUtils;

public abstract class ListRecordModel<ITEM> extends AbstractListModel implements ItemRecordConverter<ITEM> {

	protected ItemRecordModel<ITEM> recmodel = null;

	protected RecordModelChangedListener insertListener = new RecordModelChangedListener() {

		@Override
		public void recordsChanged(int from, int to) {
			fireIntervalAdded(ListRecordModel.this, from, to);
		}
	};
	protected RecordModelChangedListener deleteListener = new RecordModelChangedListener() {

		@Override
		public void recordsChanged(int from, int to) {
			fireIntervalRemoved(ListRecordModel.this, from, to);
		}
	};
	protected RecordModelChangedListener updateListener = new RecordModelChangedListener() {

		@Override
		public void recordsChanged(int row, int column) {
			fireContentsChanged(ListRecordModel.this, row, column);
		}
	};

	public ListRecordModel() {
		bindRecordModel(new ItemRecordModel<ITEM>(this));
	}

	public ListRecordModel(ItemRecordModel<ITEM> recmodel) {
		bindRecordModel(recmodel);
	}

	// ------------------------------------------------------------------------------------------------------------------------

	@Override
	public int getSize() {
		return recmodel.getRecordCount();
	}

	@Override
	public Record<ITEM> getElementAt(int row) {
		return recmodel.getRecord(row);
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public void destroy() {
		recmodel.destroy();
	}

	public void bindRecordModel(ItemRecordModel<ITEM> recmodel) {
		if(this.recmodel==recmodel) {
			return;
		}
		
		if (this.recmodel != null) {
			this.recmodel.clearRecordInsertedListeners();
			this.recmodel.clearRecordDeletedListeners();
			this.recmodel.clearRecordUpdatedListeners();
//			this.recmodel.removeRecordInsertedListener(insertListener);
//			this.recmodel.removeRecordDeletedListener(deleteListener);
//			this.recmodel.removeRecordUpdatedListener(updateListener);
		}

		this.recmodel = recmodel;

		if (recmodel != null) {
			recmodel.addRecordInsertedListener(insertListener);
			recmodel.addRecordDeletedListener(deleteListener);
			recmodel.addRecordUpdatedListener(updateListener);
		}
		
		recmodel.fireRecordsRefreshed();
	}

	public List<String> toRowString(int[] selectedRows) {
		List<String> rows = new ArrayList<String>(this.recmodel.getRecordCount() + 1);

		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < selectedRows.length; i++) {
			Record<ITEM> row = recmodel.getRecord(selectedRows[i]);

			buf.setLength(0);
			for (Iterator itrow = row.iterator(); itrow.hasNext();) {
				Object objectrow = itrow.next();
				buf.append(StringUtils.nullToString(objectrow));
				buf.append("\t");
			}
			rows.add(buf.toString());
		}
		return rows;
	}

	public List<String> toRowString() {
		List<String> rows = new ArrayList<String>(this.recmodel.getRecordCount() + 1);

		StringBuilder buf = new StringBuilder();
		rows.add(buf.toString());

		for (Iterator it = this.recmodel.iterator(); it.hasNext();) {
			Object object = (Object) it.next();
			Record row = (Record) object;

			buf.setLength(0);
			for (Iterator itrow = row.iterator(); itrow.hasNext();) {
				Object objectrow = itrow.next();
				buf.append(StringUtils.nullToString(objectrow));
				buf.append("\t");
			}
			rows.add(buf.toString());
		}
		return rows;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public Record<ITEM> addItem(ITEM item) {
		return recmodel.addItem(item);
	}

	public Record<ITEM> updateItem(ITEM item) {
		return recmodel.updateItem(item);
	}

	public Record<ITEM> updateItem(int rownum, ITEM item) {
		return recmodel.updateItem(rownum, item);
	}

	public Record<ITEM> updateinsertItem(ITEM item) {
		return recmodel.updateinsertItem(item);
	}

	public Record<ITEM> updateinsertItem(int rownum, ITEM item) {
		return recmodel.updateinsertItem(rownum, item);
	}

	public void addItems(ITEM[] items) {
		recmodel.addItems(items);
	}

	public void addItems(ITEM[] items, int size) {
		recmodel.addItems(items, size);
	}

	public Record<ITEM> insertItem(int rownum, ITEM item) {
		return recmodel.insertItem(rownum, item);
	}

	public void insertItem(int rownum, ITEM[] items) {
		recmodel.insertItem(rownum, items);
	}

	public void insertItem(int rownum, List<ITEM> items) {
		recmodel.insertItem(rownum, items);
	}

	public List<ITEM> getAllItemList() {
		return recmodel.getAllItemList();
	}

	public ITEM getItem(int selectedRow) {
		return recmodel.getItem(selectedRow);
	}

	public void removeItem(int selectedRow) {
		recmodel.removeItem(selectedRow);
	}

	public List<ITEM> getItemList(int[] selectedRows) {
		return recmodel.getItemList(selectedRows);
	}

	public void removeAllItems() {
		recmodel.removeAllItems();
	}

	public int getItemCount() {
		return recmodel.getItemCount();
	}

	// ------------------------------------------------------------------------------------------------------------------------

}