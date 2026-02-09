package com.amituofo.common.ui.listener;

public interface StatusChangedListener<META, DATA> {
	// void updateContentViewer(WorkingSpace workingSpace);

	// ContentViewer getCurrentContentViewer();
	void changed(META meta, DATA data);
}