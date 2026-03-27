package com.amituofo.common.ui.swingexts.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class RecordModel<ITEM> {

	private List<Record<ITEM>> list;
	private RecordModelCollection collectionType = RecordModelCollection.List;

	public boolean autoFireRecordsBatchInserted = true;
	public boolean autoFireRecordsInserted = true;
	public boolean autoFireRecordsDeleted = true;
	public boolean autoFireRecordsUpdated = true;

	private List<RecordModelChangedListener> recordInsertedListener = new ArrayList<>();
	private List<RecordModelChangedListener> recordDeletedListener = new ArrayList<>();
	private List<RecordModelChangedListener> recordUpdatedListener = new ArrayList<>();

	public RecordModel() {
		this(RecordModelCollection.List);
	}

	public RecordModel(RecordModelCollection collectionType) {
		this.collectionType = collectionType;
		this.list = collectionType == RecordModelCollection.List ? new ArrayList<>(1000) : new LinkedList<>();
	}

	public void destroy() {
		autoFireRecordsBatchInserted = autoFireRecordsInserted = autoFireRecordsDeleted = autoFireRecordsUpdated = false;

		recordUpdatedListener.clear();
		recordDeletedListener.clear();
		recordUpdatedListener.clear();

		this.removeRecords();
	}

	public RecordModelCollection getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(RecordModelCollection collectionType) {
		this.collectionType = collectionType;
	}

	public void addRecord(Object[] celldata) {
		if (celldata == null) {
			return;
		}

		addRecord(new Record<>(celldata));
	}

	public void addRecord(ITEM celldata) {
		if (celldata == null) {
			return;
		}

		addRecord(new Record<>(celldata));
	}

	public void addRecord(Record<ITEM> recdata) {
		if (recdata == null) {
			return;
		}

		if (autoFireRecordsInserted) {
			int rec = this.list.size();
			this.list.add(recdata);

			fireRecordsInserted(rec, rec);
		} else {
			this.list.add(recdata);
		}
	}

	public void insertRecord(int recnum, Record<ITEM> recdata) {
		if (recdata == null) {
			return;
		}

		if (autoFireRecordsInserted) {
			this.list.add(recnum, recdata);
			fireRecordsInserted(recnum, recnum);
		} else {
			this.list.add(recnum, recdata);
		}
	}

	public void removeRecord(int rec) {
		this.list.remove(rec);

		if (autoFireRecordsDeleted) {
			fireRecordsDeleted(rec, rec);
		}
	}

	public void removeRecord(Record<ITEM> recdata) {
		if (recdata == null) {
			return;
		}

		int index = list.indexOf(recdata);
		this.list.remove(index);

		if (autoFireRecordsDeleted) {
			fireRecordsDeleted(index, index);
		}
	}

	public void removeRecords() {
		int old = getRecordCount();
		if (old == 0) {
			return;
		}

		List<Record<ITEM>> newdatarecs = collectionType == RecordModelCollection.List ? new ArrayList<>(1000) : new LinkedList<>();
		this.list = newdatarecs;

		if (autoFireRecordsDeleted) {
			fireRecordsDeleted(0, old - 1);
		}
	}

	public void setRecord(int rec, Record<ITEM> recdata) {
		list.set(rec, recdata);
		if (autoFireRecordsUpdated) {
			fireRecordsUpdated(rec, 0);
		}
	}

	public void setRecordCellValue(Object aValue, int rec, int column) {
		Record recdata = getRecord(rec);
		if (recdata != null) {
			recdata.set(column, aValue);

			if (autoFireRecordsUpdated) {
				fireRecordsUpdated(rec, column);
			}
		}
	}

	public void forEach(Consumer<? super Record<ITEM>> action) {
		list.forEach(action);
	}

	public Iterator<Record<ITEM>> iterator() {
		return list.iterator();
	}

	public int getRecordCount() {
		return this.list.size();
	}

	public Record<ITEM> getRecord(int rec) {
		try {
			return (Record<ITEM>) this.list.get(rec);
		} catch (Exception e) {
			// Table 视图模型和Model模型数据不统一导致 repain 数组越界问题！！
//			e.printStackTrace();
			System.err.println("Ignore this. " + e.getMessage());
		}

		return null;
	}

	public List<Record<ITEM>> getRecordList(int[] recs) {
		List<Record<ITEM>> items = new ArrayList<>(recs.length);
		for (int i = 0; i < recs.length; i++) {
			Record<ITEM> item = this.getRecord(recs[i]);
			if (item != null) {
				items.add(item);
			}
		}
		return items;
	}

	public Object getRecordCellValue(int rec, int column) {
		Record recdata = getRecord(rec);
		if (recdata != null) {
			return recdata.get(column);
		}

		return null;
	}

	public List<Record<ITEM>> ofList() {
		return list;
	}

	public ITEM[] records2Array(ITEM[] items) {
		int size = Math.min(items.length, this.list.size());
		for (int i = 0; i < size; i++) {
			items[i] = getRecord(i).getUserData();
		}

		return items;
	}

	// ------------------------------------------------------------------------------------------------------------------------

	public RecordModel<ITEM> setAutoFireRecordsBatchInserted(boolean autoFireRecordsBatchInserted) {
		this.autoFireRecordsBatchInserted = autoFireRecordsBatchInserted;
		return this;
	}

	public RecordModel<ITEM> setAutoFireRecordsInserted(boolean autoFireRecordsInserted) {
		this.autoFireRecordsInserted = autoFireRecordsInserted;
		return this;
	}

	public RecordModel<ITEM> setAutoFireRecordsDeleted(boolean autoFireRecordsDeleted) {
		this.autoFireRecordsDeleted = autoFireRecordsDeleted;
		return this;
	}

	public RecordModel<ITEM> setAutoFireRecordsUpdated(boolean autoFireRecordsUpdated) {
		this.autoFireRecordsUpdated = autoFireRecordsUpdated;
		return this;
	}

	public void clearRecordInsertedListeners() {
		recordInsertedListener.clear();
	}

	public void clearRecordDeletedListeners() {
		recordDeletedListener.clear();
	}

	public void clearRecordUpdatedListeners() {
		recordUpdatedListener.clear();
	}

	public void addRecordInsertedListener(RecordModelChangedListener listener) {
		if (listener == null) {
			return;
		}
		recordInsertedListener.add(listener);
	}

	public void addRecordDeletedListener(RecordModelChangedListener listener) {
		if (listener == null) {
			return;
		}
		recordDeletedListener.add(listener);
	}

	public void addRecordUpdatedListener(RecordModelChangedListener listener) {
		if (listener == null) {
			return;
		}
		recordUpdatedListener.add(listener);
	}

	public void removeRecordInsertedListener(RecordModelChangedListener listener) {
		if (listener == null) {
			return;
		}
		recordInsertedListener.remove(listener);
	}

	public void removeRecordDeletedListener(RecordModelChangedListener listener) {
		if (listener == null) {
			return;
		}
		recordDeletedListener.remove(listener);
	}

	public void removeRecordUpdatedListener(RecordModelChangedListener listener) {
		if (listener == null) {
			return;
		}
		recordUpdatedListener.remove(listener);
	}

	public void fireRecordsInserted(int from, int to) {
		for (RecordModelChangedListener listener : recordInsertedListener) {
			listener.recordsChanged(from, to);
		}
	}

	public void fireRecordsDeleted(int from, int to) {
		for (RecordModelChangedListener listener : recordDeletedListener) {
			listener.recordsChanged(from, to);
		}
	}

	public void fireRecordsUpdated(int rec, int column) {
		for (RecordModelChangedListener listener : recordUpdatedListener) {
			listener.recordsChanged(rec, column);
		}
	}

	public void fireRecordsRefreshed() {
		if (this.list.isEmpty()) {
			return;
		}

		fireRecordsInserted(0, getRecordCount() - 1);
	}

	// ------------------------------------------------------------------------------------------------------------------------

}