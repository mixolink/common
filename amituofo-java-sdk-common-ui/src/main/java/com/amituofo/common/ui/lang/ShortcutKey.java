package com.amituofo.common.ui.lang;

import javax.swing.KeyStroke;

import com.amituofo.common.util.StringUtils;
import com.amituofo.common.util.SystemUtils;

public class ShortcutKey {
	private final static char SP = (char) 31;

	private String actionID;
	private int keyCode = -1;
	private int modifiers = -1;
	private String comment = "";

	public ShortcutKey(String actionID, int keyCode, int modifiers) {
		super();
		this.actionID = actionID;
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}

	public static ShortcutKey parse(String encodeString) {
		if (encodeString == null) {
			return null;
		}

		String[] vs = StringUtils.split(encodeString, SP);
		if (vs.length != 3) {
			return null;
		}

		return new ShortcutKey(vs[0], Integer.parseInt(vs[1]), Integer.parseInt(vs[2]));
	}

	public String getActionId() {
		return actionID;
	}

	public boolean isEnabled() {
		return modifiers != -1;
	}

	public String encodeString() {
		return actionID + SP + keyCode + SP + modifiers;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public KeyStroke toKeyStroke() {
		if (!isEnabled()) {
			return null;
		}
		return KeyStroke.getKeyStroke(keyCode, modifiers);
	}
}
