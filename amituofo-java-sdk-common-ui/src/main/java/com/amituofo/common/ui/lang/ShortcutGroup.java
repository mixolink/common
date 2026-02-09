package com.amituofo.common.ui.lang;

import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;

import com.amituofo.common.util.StringUtils;

public class ShortcutGroup {
	private static ShortcutGroup instance = new ShortcutGroup();

	private Map<String, ShortcutKeys> keyMap = new HashMap<>();

	private ShortcutGroup() {
	}

	public static ShortcutGroup Default() {
		return instance;
	}

	public ShortcutKeys get(String id) {
		ShortcutKeys sks = keyMap.get(id);
		if (sks == null) {
			sks = new ShortcutKeys(id, "");
			keyMap.put(id, sks);
		}
		return sks;
	}

}
