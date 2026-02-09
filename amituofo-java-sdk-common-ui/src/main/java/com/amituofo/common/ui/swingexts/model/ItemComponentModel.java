package com.amituofo.common.ui.swingexts.model;

public interface ItemComponentModel<ITEM> {

	void removeAllItems();

	void addItems(ITEM[] itemPage, int totalSize);
}
