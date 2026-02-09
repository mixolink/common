package com.amituofo.common.kit.kv;

public class StringTitle extends NameValue<String> {

	public StringTitle(String name, String value) {
		super(name, value);
	}

	@Override
	public String toString() {
		return getName();
	}
}
