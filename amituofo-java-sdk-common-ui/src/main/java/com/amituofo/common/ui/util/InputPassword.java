package com.amituofo.common.ui.util;

public class InputPassword {
	private char[] password;
	boolean confirmed;

	public InputPassword(char[] password, boolean confirmed) {
		super();
		this.password = password;
		this.confirmed = confirmed;
	}

	public boolean isCancel() {
		return !confirmed;
	}

	public boolean isConfirm() {
		return confirmed;
	}

	public char[] getPassword() {
		return password;
	}

	public String getPasswordString() {
		return new String(password);
	}

	public boolean isEmpty() {
		return password == null || password.length == 0;
	}

	public static InputPassword ok(char[] pwd) {
		return new InputPassword(pwd, true);
	}

	public static InputPassword cancel() {
		return new InputPassword(null, false);
	}
}
