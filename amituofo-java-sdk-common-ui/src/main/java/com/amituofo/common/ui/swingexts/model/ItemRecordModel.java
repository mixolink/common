package com.amituofo.common.ui.swingexts.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.Destroyable;
import com.amituofo.common.api.ObjectHandler;
import com.amituofo.common.type.HandleFeedback;

public class ItemRecordModel<ITEM> extends RecordModel<ITEM> implements ItemComponentModel<ITEM>, Destroyable {
	private ItemRecordConverter<ITEM> convert;

	public ItemRecordModel(ItemRecordConverter<ITEM> convert) {
		super();
		this.convert = convert;
	}

	public ItemModelDataInjector<ITEM> buildModelHelper() {
		return new ItemModelDataInjector<ITEM>(this);
	}

	public void setItemRecordConverter(ItemRecordConverter<ITEM> convert) {
		this.convert = convert;
	}

	public Record<ITEM> addItem(ITEM item) {
		if (item == null) {
			return null;
		}

		int rownum = getRecordCount();
		Record<ITEM> rowData = convert.convertRow0(rownum, item);
		if (rowData != null) {
			rowData.setUserData(item);
			addRecord(rowData);
		}

		return rowData;
	}

	public Record<ITEM> updateItem(ITEM item) {
		if (item == null) {
			return null;
		}

		int size = getRecordCount();
		for (int i = 0; i < size; i++) {
			Record<ITEM> datarow = (Record<ITEM>) getRecord(i);
			if (datarow.getUserData().equals(item)) {
				Record<ITEM> rowData = convert.convertRow0(i, item);
				rowData.setUserData(item);
				setRecord(i, rowData);
				return datarow;
			}
		}

		return null;
	}

	public Record<ITEM> updateItem(int rownum, ITEM item) {
		if (item == null || rownum < 0) {
			return null;
		}

		int size = getRecordCount();
		if (rownum >= size) {
			return null;
		}

		Record<ITEM> datarow = (Record<ITEM>) getRecord(rownum);
		if (datarow.getUserData().equals(item)) {
			datarow = convert.convertRow0(rownum, item);
			datarow.setUserData(item);
			setRecord(rownum, datarow);
			return datarow;
		}

		return null;
	}

	public Record<ITEM> updateinsertItem(ITEM item) {
		if (item == null) {
			return null;
		}

		Record<ITEM> row = updateItem(item);
		if (row == null) {
			return addItem(item);
		}

		return row;
	}

	public Record<ITEM> updateinsertItem(int rownum, ITEM item) {
		if (item == null) {
			return null;
		}

		Record<ITEM> row = updateItem(rownum, item);
		if (row == null) {
			return insertItem(rownum, item);
		}

		return row;
	}

	public void addItems(ITEM[] items) {
		addItems(items, items.length);
	}

	public void addItems(ITEM[] items, int size) {
		if (items == null || items.length == 0 || size > items.length) {
			return;
		}

		for (int i = 0; i < size; i++) {
			ITEM item = items[i];
			if (item == null) {
				continue;
			}

			Record<ITEM> rowData = convert.convertRow0(i, item);

			if (rowData != null) {
				rowData.setUserData(item);
				addRecord(rowData);
			}
		}
	}
	

	public void addItems(List<ITEM> items) {
		addItems(items, items.size());
	}

	public void addItems(List<ITEM> items, int size) {
		if (items == null || items.size() == 0 || size > items.size()) {
			return;
		}

		for (int i = 0; i < size; i++) {
			ITEM item = items.get(i);
			if (item == null) {
				continue;
			}

			Record<ITEM> rowData = convert.convertRow0(i, item);

			if (rowData != null) {
				rowData.setUserData(item);
				addRecord(rowData);
			}
		}
	}

	public Record<ITEM> insertItem(int rownum, ITEM item) {
		if (item == null) {
			return null;
		}

		Record<ITEM> row = convert.convertRow0(rownum, item);
		if (row != null) {
			row.setUserData(item);
			insertRecord(rownum, row);
		}

		return row;
	}

	public void insertItem(int rownum, ITEM[] items) {
		if (items == null || items.length == 0 || rownum < 0) {
			return;
		}

		for (ITEM item : items) {
			if (item == null) {
				continue;
			}

			Record<ITEM> row = convert.convertRow0(rownum, item);
			if (row != null) {
				row.setUserData(item);
				insertRecord(rownum, row);
			}
		}
	}

	public void insertItem(int rownum, List<ITEM> items) {
		if (items == null || items.isEmpty() || rownum < 0) {
			return;
		}

		for (ITEM item : items) {
			if (item == null) {
				continue;
			}

			Record<ITEM> row = convert.convertRow0(rownum, item);
			if (row != null) {
				row.setUserData(item);
				insertRecord(rownum, row);
			}
		}
	}

	public List<ITEM> getAllItemList() {
		int size = getRecordCount();
		List<ITEM> items = new ArrayList<ITEM>(size);
		for (Record<ITEM> row : ofList()) {
			items.add(row.getUserData());
		}

		return items;

//		List<ITEM> items = this.datarows.stream()
//                .map(RowData::getUserData)
//                .collect(Collectors.toList());
//		return items;
	}

//	public List<ModelIndexData<ITEM>> getAllItemListWithModelIndex() {
//		int size = getRecordCount();
//		List<ModelIndexData<ITEM>> items = new ArrayList<>(size);
//		for (int i = 0; i < size; i++) {
//			ITEM item = this.getItem(i);
//			if (item != null) {
//				items.add(new ModelIndexData<ITEM>(i, item));
//			}
//		}
//		return items;
//	}

	public ITEM getItem(int selectedRow) {
		Record<ITEM> row = getRecord(selectedRow);
		if (row != null) {
			return row.getUserData();
		}
		return null;
	}

//	public Record<ITEM> getRow(int row) {
//		return (Record<ITEM>) getRecord(row);
//	}

	public List<ITEM> getItemList(int[] rows) {
		List<ITEM> items = new ArrayList<ITEM>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			ITEM item = this.getItem(rows[i]);
			if (item != null) {
				items.add(item);
			}
		}
		return items;
	}

	public List<ModelIndexData<ITEM>> getItemListWithModelIndex(int[] rows) {
		List<ModelIndexData<ITEM>> items = new ArrayList<>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			ITEM item = this.getItem(rows[i]);
			if (item != null) {
				items.add(new ModelIndexData<ITEM>(rows[i], item));
			}
		}
		return items;
	}

	@Override
	public void removeAllItems() {
		removeRecords();
	}

	public int getItemCount() {
		return getRecordCount();
	}

	public void removeItem(int selectedRow) {
		removeRecord(selectedRow);
	}

}
