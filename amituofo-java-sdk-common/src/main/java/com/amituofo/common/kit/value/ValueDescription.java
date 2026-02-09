package com.amituofo.common.kit.value;

public class ValueDescription<T> {

	private T o;
	private String description;

	public ValueDescription(T o) {
		this.o = o;
	}

	public ValueDescription(T o, String description) {
		this.o = o;
		this.description = description;
	}

	public T getValue() {
		return o;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}

	@Override
	public int hashCode() {
		return o.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueDescription other = (ValueDescription) obj;
		if (o == null) {
			if (other.o != null)
				return false;
		} else if (!o.equals(other.o))
			return false;
		return true;
	}

}
