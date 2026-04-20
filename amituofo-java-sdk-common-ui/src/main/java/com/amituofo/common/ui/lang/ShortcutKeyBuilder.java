package com.amituofo.common.ui.lang;

import java.awt.event.KeyEvent;

import com.amituofo.common.util.SystemUtils;

public class ShortcutKeyBuilder {
	private int keyCode = 0;
	private int modifiers = 0;
	private String actionID;

	public ShortcutKeyBuilder(String actionID) {
		super();
		this.actionID = actionID;
	}

//	public static ShortcutKeyBuilder CommandKey(String id) {
//		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);
//
//		builder.withKey(_COMMAND_KEY);
//
//		return builder;
//	}

	public static ShortcutKeyBuilder Action(String actionID) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(actionID);

		return builder;
	}

	public ShortcutKeyBuilder withCmd() {
		this.withModifier(KeyEvent.META_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withCmdOrCtrl() {
		this.withModifier(SystemUtils.isWindows() ? KeyEvent.CTRL_DOWN_MASK : KeyEvent.META_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withCmdShift() {
		this.withModifier(KeyEvent.META_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withCtrl() {
		this.withModifier(KeyEvent.CTRL_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withOption() {
		this.withModifier(KeyEvent.ALT_DOWN_MASK);
		return this;
	}
	
	public ShortcutKeyBuilder withAlt() {
		this.withModifier(KeyEvent.ALT_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withShift() {
		this.withModifier(KeyEvent.SHIFT_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withCtrlShift() {
		this.withModifier(KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withCtrlAlt() {
		this.withModifier(KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withAltShift() {
		this.withModifier(KeyEvent.ALT_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
		return this;
	}

	public ShortcutKeyBuilder withKey(int keyCode) {
		this.keyCode = keyCode;
		return this;
	}

	public ShortcutKeyBuilder withModifier(int modifier) {
		this.modifiers |= modifier;
		return this;
	}

	public ShortcutKey build() {
		return new ShortcutKey(actionID, keyCode, modifiers);
	}
}
