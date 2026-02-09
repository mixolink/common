package com.amituofo.common.ui.lang;

public class CardComponent<C, D> {
	String name;
	C component;
	D data;

	public CardComponent(String name, C component, D data) {
		super();
		this.name = name;
		this.component = component;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public C getComponent() {
		return component;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

}
