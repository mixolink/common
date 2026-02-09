package com.amituofo.common.ui.swingexts.model;

public interface ItemRecordConverter<ITEM> {

	Record<ITEM> convertRow0(int rownum, ITEM item);
}
