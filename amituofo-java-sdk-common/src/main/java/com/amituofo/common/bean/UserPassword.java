package com.amituofo.common.bean;

public class UserPassword {
	private String userId;
	private String username;
	private String oldPasword;
	private String newPasword;

	public UserPassword() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPasword() {
		return oldPasword;
	}

	public void setOldPasword(String oldPasword) {
		this.oldPasword = oldPasword;
	}

	public String getNewPasword() {
		return newPasword;
	}

	public void setNewPasword(String newPasword) {
		this.newPasword = newPasword;
	}

}
