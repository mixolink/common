package com.amituofo.common.ui.lang;

import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;

import com.amituofo.common.util.StringUtils;

public class ShortcutKeys {
//	private static ShortcutKeys instance = new ShortcutKeys("Default");

	private Map<String, ShortcutKey> keyMap = new HashMap<>();

	private String group;
	private String comment;

	public ShortcutKeys(String group, String comment) {
		this.group = group;
		this.comment = comment;
	}

//	public static ShortcutKeys Default() {
//		return instance;
//	}

//	public static ShortcutKeys parse(String encodeString) {
//		ShortcutKeys sks = new ShortcutKeys();
//		if (encodeString == null) {
//			return sks;
//		}
//
//		encodeString = StringUtils.decodeBase64ToString(encodeString);
//
//		int i = encodeString.indexOf(((char) 30));
//		if (i == -1) {
//			return sks;
//		}
//
//		String[] skarray = StringUtils.split(encodeString, (char) 30);
//		for (String encodesk : skarray) {
//			ShortcutKey skey = ShortcutKey.parse(encodesk);
//			if (skey != null)
//				sks.keyMap.put(encodeString, skey);
//		}
//
//		return sks;
//	}

	public ShortcutKeys parse(String encodeString) {
		if (encodeString == null) {
			return this;
		}

		encodeString = StringUtils.decodeBase64ToString(encodeString);

		int i = encodeString.indexOf(((char) 30));
		if (i == -1) {
			return this;
		}

		String[] skarray = StringUtils.split(encodeString, (char) 30);
		for (String encodesk : skarray) {
			ShortcutKey skey = ShortcutKey.parse(encodesk);
			if (skey != null)
				this.keyMap.put(encodeString, skey);
		}

		return this;
	}

	public String encodeString() {
		StringBuffer buf = new StringBuffer();
		for (ShortcutKey sk : keyMap.values()) {
			if (sk.isEnabled()) {
				buf.append(sk.encodeString()).append((char) 30);
			}
		}
		return StringUtils.encodeBase64String(buf.toString());
	}

	public ShortcutKey getShortcutKey(String actionID) {
		return keyMap.get(actionID);
	}

//	public void setShortcutKey(String id, String keyStroke) {
//		keyMap.put(id, new ShortcutKey(id, keyStroke));
//	}

	public void setShortcutKey(ShortcutKey sk) {
		keyMap.put(sk.getActionId(), sk);
	}

	public KeyStroke getShortcutKeyStroke(String id) {
		ShortcutKey sk = keyMap.get(id);
		if (sk == null) {
			return null;
		}

		return sk.toKeyStroke();
	}
}
