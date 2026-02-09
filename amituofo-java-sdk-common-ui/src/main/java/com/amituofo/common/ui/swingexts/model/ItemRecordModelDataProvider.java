package com.amituofo.common.ui.swingexts.model;

import com.amituofo.common.api.ObjectHandler;

public interface ItemRecordModelDataProvider<ITEM> {

//	Record<ITEM> convertRow0(int rownum, ITEM item);

	void list(ObjectHandler<Integer, ITEM> event, Object... args);

	void destroy();
}
