package com.amituofo.common.ex;

public class TwoFactorAuthRequiredException extends RuntimeException {

	private String securityDeviceName;
	private boolean invalidCode;

	public TwoFactorAuthRequiredException() {
		super();
	}

	public TwoFactorAuthRequiredException(String message) {
		super(message);
	}

	public TwoFactorAuthRequiredException(String securityDeviceName, String message) {
		super(message);
		this.securityDeviceName = securityDeviceName;
	}

	public TwoFactorAuthRequiredException(String securityDeviceName, String message, boolean invalidCode) {
		super(message);
		this.securityDeviceName = securityDeviceName;
		this.invalidCode = invalidCode;
	}

	public String getSecurityDeviceName() {
		return securityDeviceName;
	}

	public boolean isInvalidCode() {
		return invalidCode;
	}

}
