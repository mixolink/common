package com.amituofo.common.ui.util;

import com.amituofo.common.util.StringUtils;

public class InputUserPassword extends InputPassword {
	private String username;

	public InputUserPassword(String username, char[] password, boolean confirmed) {
		super(password, confirmed);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public static InputUserPassword ok(String username, char[] pwd) {
		return new InputUserPassword(username, pwd, true);
	}

	public static InputUserPassword cancel() {
		return new InputUserPassword(null, null, false);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(username) || super.isEmpty();
	}
}
