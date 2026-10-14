package com.amituofo.common.bean;

public class InputMFACode {
	private char[] code;
	boolean confirmed;

	public InputMFACode(char[] code, boolean confirmed) {
		super();
		this.code = code;
		this.confirmed = confirmed;
	}

	public boolean isCancel() {
		return !confirmed;
	}

	public boolean isConfirm() {
		return confirmed;
	}

	public char[] getCode() {
		return code;
	}

	public void clearCode() {
		code = null;
	}

	public boolean isEmpty() {
		return code == null || code.length == 0;
	}

	public static InputMFACode ok(char[] pwd) {
		return new InputMFACode(pwd, true);
	}

	public static InputMFACode cancel() {
		return new InputMFACode(null, false);
	}
}
