package com.amituofo.common.ex;

import com.amituofo.common.type.UserIdentityResult;

public class AuthException extends RuntimeException {
	UserIdentityResult identityCode;

	public AuthException(UserIdentityResult identityCode) {
		super();
		this.identityCode = identityCode;
	}

	public AuthException(UserIdentityResult identityCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.identityCode = identityCode;
	}

	public AuthException(UserIdentityResult identityCode, String message, Throwable cause) {
		super(message, cause);
		this.identityCode = identityCode;
	}

	public AuthException(UserIdentityResult identityCode, String message) {
		super(message);
		this.identityCode = identityCode;
	}

	public AuthException(UserIdentityResult identityCode, Throwable cause) {
		super(cause);
		this.identityCode = identityCode;
	}

	public UserIdentityResult getIdentityCode() {
		return identityCode;
	}

}
