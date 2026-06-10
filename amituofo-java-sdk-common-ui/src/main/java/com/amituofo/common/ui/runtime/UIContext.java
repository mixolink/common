package com.amituofo.common.ui.runtime;

import javax.swing.JFrame;

public class UIContext {
	private static boolean allDestroyed = false;
	private static JFrame DEFAULT_MAIN_FRAME = null;

	public UIContext() {
	}

	public static boolean isAllDestroyed() {
		return allDestroyed;
	}

	public static void setAllDestroyed(boolean allDestroyed) {
		UIContext.allDestroyed = allDestroyed;
	}

	public static JFrame getDefaultTopFrame() {
		return DEFAULT_MAIN_FRAME;
	}

	public static void setDefaultTopFrame(JFrame c) {
		DEFAULT_MAIN_FRAME = c;
	}

}
