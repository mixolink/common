package com.amituofo.common.ui.lang;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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
	
	@Override
	public String toString() {
	    if (!isEnabled()) {
	        return "";
	    }

		boolean IS_MAC = SystemUtils.isMacOS();

	    StringBuilder sb = new StringBuilder();

	    if ((modifiers & InputEvent.META_DOWN_MASK) != 0) {
	        sb.append(IS_MAC ? "⌘" : "Meta+");
	    }
	    if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
	        sb.append(IS_MAC ? "⌃" : "Ctrl+");
	    }
	    if ((modifiers & InputEvent.ALT_DOWN_MASK) != 0) {
	        sb.append(IS_MAC ? "⌥" : "Alt+");
	    }
	    if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
	        sb.append(IS_MAC ? "⇧" : "Shift+");
	    }

	    if (keyCode == KeyEvent.VK_ENTER) {
	        sb.append(IS_MAC ? "↩" : "Enter");
	    } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
	        sb.append(IS_MAC ? "⌫" : "Backspace");
	    } else if (keyCode == KeyEvent.VK_DELETE) {
	        sb.append(IS_MAC ? "⌦" : "Delete");
	    } else if (keyCode == KeyEvent.VK_ESCAPE) {
	        sb.append(IS_MAC ? "⎋" : "Esc");
	    } else if (keyCode == KeyEvent.VK_TAB) {
	        sb.append(IS_MAC ? "⇥" : "Tab");
	    } else if (keyCode == KeyEvent.VK_SPACE) {
	        sb.append(IS_MAC ? "␣" : "Space");
	    } else if (keyCode == KeyEvent.VK_UP) {
	        sb.append("↑");
	    } else if (keyCode == KeyEvent.VK_DOWN) {
	        sb.append("↓");
	    } else if (keyCode == KeyEvent.VK_LEFT) {
	        sb.append("←");
	    } else if (keyCode == KeyEvent.VK_RIGHT) {
	        sb.append("→");
	    } else if (keyCode == KeyEvent.VK_HOME) {
	        sb.append("Home");
	    } else if (keyCode == KeyEvent.VK_END) {
	        sb.append("End");
	    } else if (keyCode == KeyEvent.VK_PAGE_UP) {
	        sb.append("PgUp");
	    } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
	        sb.append("PgDn");
	    } else if (keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12) {
	        sb.append("F").append(keyCode - KeyEvent.VK_F1 + 1);
	    } else {
	        sb.append(KeyEvent.getKeyText(keyCode));
	    }

	    return sb.toString();
	}
}
