package com.amituofo.common.kit.parser;

public class FunctionDesc {
	String name;
	String[] params;

	public FunctionDesc(String name, String[] params) {
		super();
		this.name = name;
		this.params = params;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Object[] getParams() {
//		return params;
//	}

	public String[] getStringParams() {
		return (String[]) params;
	}

	public String[] upperCaseParams() {
		for (int i = 0; i < params.length; i++) {
			params[i] = params[i].toUpperCase();
		}

		return params;
	}

	public String[] lowerCaseParams() {
		for (int i = 0; i < params.length; i++) {
			params[i] = params[i].toLowerCase();
		}

		return params;
	}

	public String[] trimParams() {
		for (int i = 0; i < params.length; i++) {
			params[i] = params[i].trim();
		}

		return params;
	}

	public void setStringParams(String[] params) {
		this.params = params;
	}

	public String getFirstStringParams() {
		return (params == null || params.length == 0) ? null : params[0];
	}

}
