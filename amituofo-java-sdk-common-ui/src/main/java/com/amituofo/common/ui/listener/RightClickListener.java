package com.amituofo.common.ui.listener;

import java.awt.event.MouseEvent;
import java.util.List;

public interface RightClickListener<ITEM> {

	void rightClicked(MouseEvent e, List<ITEM> selectedItems);

}
