package com.amituofo.common.ui.swingexts.model;

import java.util.ArrayList;
import java.util.Objects;

public class Record<T> extends ArrayList<Object> {
	private boolean disabled = false;
	private T userData;

	public Record(int initialCapacity) {
		super(initialCapacity);
	}

	public Record(Object[] celldata) {
		super(celldata.length);
		for (Object o : celldata) {
			add(o);
		}
	}

	public Record(Object celldata) {
		super(1);
		add(celldata);
	}

	public T getUserData() {
		return userData;
	}

	public void setUserData(T userData) {
		this.userData = userData;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disable) {
		this.disabled = disable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(userData);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		return Objects.equals(userData, other.userData);
	}

}
