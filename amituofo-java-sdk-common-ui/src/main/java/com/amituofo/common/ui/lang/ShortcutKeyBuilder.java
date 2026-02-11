package com.amituofo.common.ui.lang;

import java.awt.event.KeyEvent;

import com.amituofo.common.util.SystemUtils;

public class ShortcutKeyBuilder {
	private int keyCode = 0;
	private int modifiers = 0;
	private String id;

	public ShortcutKeyBuilder(String id) {
		super();
		this.id = id;
	}

//	public static ShortcutKeyBuilder CommandKey(String id) {
//		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);
//
//		builder.withKey(_COMMAND_KEY);
//
//		return builder;
//	}

	public static ShortcutKeyBuilder Default(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		return builder;
	}

	public static ShortcutKeyBuilder Cmd(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.META_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder CmdOrCtrl(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(SystemUtils.isWindows() ? KeyEvent.CTRL_DOWN_MASK : KeyEvent.META_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder Ctrl(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.CTRL_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder Alt(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.ALT_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder Shift(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.SHIFT_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder CtrlShift(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder CtrlAlt(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK);

		return builder;
	}

	public static ShortcutKeyBuilder AltShift(String id) {
		ShortcutKeyBuilder builder = new ShortcutKeyBuilder(id);

		builder.withModifier(KeyEvent.ALT_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);

		return builder;
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
		return new ShortcutKey(id, keyCode, modifiers);
	}
}
