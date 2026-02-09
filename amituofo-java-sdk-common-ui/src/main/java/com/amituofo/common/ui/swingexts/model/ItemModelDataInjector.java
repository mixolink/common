package com.amituofo.common.ui.swingexts.model;

public class ItemModelDataInjector<ITEM> {
	public final static int PAGE_SIZE = 123;
	private Object[] tempItemAddArray = new Object[PAGE_SIZE];
	private int i4add = 0;

//	private int[] tempItemInsertRowArray = new int[PAGE_SIZE];
//	private Object[] tempItemInsertArray = new Object[PAGE_SIZE];
//	private int i4insert = 0;
	private ItemRecordModel<ITEM> tableModel;
	private boolean interrupt = false;

	public ItemModelDataInjector(ItemRecordModel<ITEM> tableModel) {
		this.tableModel = tableModel;
	}

	public void flush() {
		if (i4add > 0) {
			final int totalSize = i4add;
			final ITEM[] itemPage = (ITEM[]) tempItemAddArray;

			tempItemAddArray = new Object[PAGE_SIZE];
			i4add = 0;

			tableModel.addItems(itemPage, totalSize);
		}

//		if (i4insert > 0) {
//			final ITEM[] itemPage = (ITEM[]) tempItemInsertArray;
//			final int[] itemIndexPage = tempItemInsertRowArray;
//			tableModel.insert(itemIndexPage, itemPage);
//			tempItemInsertArray = new Object[PAGE_SIZE];
//			tempItemInsertRowArray = new int[PAGE_SIZE];
//			i4insert = 0;
//		}
	}

	public void add(ITEM data) {
		if (data == null || interrupt) {
			flush();
			return;
		}

		tempItemAddArray[i4add++] = data;

		if (i4add == PAGE_SIZE) {
			flush();
		}
	}

//	public void insert(int row, ITEM data) {
//		if (data == null || interrupt) {
//			flush();
//			return;
//		}
//
//		i4insert++;
//		tempItemInsertArray[i4insert] = data;
//		tempItemInsertRowArray[i4insert] = row;
//
//		if (i4insert == PAGE_SIZE) {
//			flush();
//		}
//	}

	public void interrupt() {
		interrupt = true;
		flush();

//		tempItemAddArray = null;
//		i4add = 0;
//
//		tempItemInsertRowArray = null;
//		tempItemInsertArray = null;
//		i4insert = 0;
	}

	public synchronized void destroy() {
		interrupt = true;
		i4add = 0;
//		i4insert = 0;
		tempItemAddArray = null;
//		tempItemInsertRowArray = null;
//		tempItemInsertArray = null;
		tableModel = null;
	}

	public void reset() {
		if (tableModel != null) {
			tableModel.removeAllItems();
			tableModel = null;
		}
	}

	public boolean isInterruptted() {
		return interrupt;
	}
}
