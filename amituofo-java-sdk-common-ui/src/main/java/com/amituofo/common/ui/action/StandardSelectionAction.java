package com.amituofo.common.ui.action;

import com.amituofo.common.api.DataFilter;
import com.amituofo.common.ui.lang.SelectGroup;

public interface StandardSelectionAction<DATA> {
	void select(SelectGroup items);

	void select(DataFilter<DATA> filter);
}
