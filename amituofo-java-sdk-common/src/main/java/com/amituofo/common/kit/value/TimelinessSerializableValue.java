package com.amituofo.common.kit.value;

import java.io.Serializable;

import com.amituofo.common.api.ValueGetter;

public class TimelinessSerializableValue<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = -2743122920776169867L;
	private final T defaultValue;
	private final long lifetime;

	private T value = null;
	private long lastUpdateTime = 0;
	private ValueGetter<T> valueGatter = null;

	public TimelinessSerializableValue(int lifetime) {
		this(null, null, lifetime, null);
	}

//	public TimelinessValue(T defaultValue, int lifetime) {
//		this(defaultValue, defaultValue, lifetime, null);
//	}

	public TimelinessSerializableValue(long lifetime, ValueGetter<T> valueGatter) {
		this(null, null, lifetime, valueGatter);
	}

	public TimelinessSerializableValue(T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		this(null, defaultValue, lifetime, valueGatter);
	}

	public TimelinessSerializableValue(T value, T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		this.defaultValue = defaultValue;
		this.lifetime = lifetime;
		this.valueGatter = valueGatter;

		setValue(value);
	}

	public boolean isExpired() {
		return (System.currentTimeMillis() - lastUpdateTime) > lifetime;
	}

	public T getValue() {
		if (lifetime <= 0) {
			return value;
		}

		if (isExpired()) {
			value = null;
		}

		return updateValue();
	}

	public void setValue(T value) {
		this.value = value;
		lastUpdateTime = System.currentTimeMillis();
	}

	private T updateValue() {
		if (value != null) {
			return value;
		}

		if (valueGatter != null) {
			T val = valueGatter.getValue();
			if (val != null) {
				setValue(val);
				return value;
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}

	}

}
