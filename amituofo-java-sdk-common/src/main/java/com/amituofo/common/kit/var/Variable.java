package com.amituofo.common.kit.var;

import com.amituofo.common.api.StringValueReplacer;

public class Variable {
	private String name;
	private StringValueReplacer valueReplacer;

	public Variable(String name, StringValueReplacer valueReplacer) {
		super();
		this.name = name;
		this.valueReplacer = valueReplacer;
	}

	public String getName() {
		return name;
	}

	public StringValueReplacer getValueReplacer() {
		return valueReplacer;
	}

}
