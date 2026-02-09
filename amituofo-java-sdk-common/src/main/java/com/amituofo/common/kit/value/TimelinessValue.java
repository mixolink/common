package com.amituofo.common.kit.value;

import com.amituofo.common.api.ValueGetter;

public class TimelinessValue<T> {
	protected final T defaultValue;
	protected final long lifetime;

	protected T value = null;
	protected long lastSetTime = 0;
	protected ValueGetter<T> valueGatter = null;

	public TimelinessValue(long lifetime) {
		this(null, null, lifetime, null);
	}

	public TimelinessValue(T value, int lifetime) {
		this(value, null, lifetime, null);
	}

//	public TimelinessValue(T value, long expireTime) {
//		this(value, null, 0, null);
//	}

	public TimelinessValue(long lifetime, ValueGetter<T> valueGatter) {
		this(null, null, lifetime, valueGatter);
	}

	public TimelinessValue(T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		this(null, defaultValue, lifetime, valueGatter);
	}

	public TimelinessValue(T value, T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		this.defaultValue = defaultValue;
		this.lifetime = lifetime;
		this.valueGatter = valueGatter;

		setValue(value);
	}

	public boolean isExpired() {
		if (lifetime <= 0) {
			return false;
		}

		return (System.currentTimeMillis() - lastSetTime) > lifetime;
	}

	public T getValue(boolean autoUpdate) {
		if (autoUpdate == false) {
			return value;
		}

		if (lifetime <= 0) {
			return value;
		}

		if (value != null && isExpired()) {
			value = null;
		}

		return updateValue();
	}

	public T getValue() {
		return getValue(true);
	}

	public void setValue(T value) {
		this.value = value;
		lastSetTime = System.currentTimeMillis();
	}

	public long getLastUpdateTime() {
		return lastSetTime;
	}

	public void resetLastUpdateTime(long lastUpdateTime) {
		this.lastSetTime = lastUpdateTime;
	}

	public void resetValue() {
		expireNow();
		updateValue();
	}

	protected T updateValue() {
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

	public void expireNow() {
		lastSetTime = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimelinessValue other = (TimelinessValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
