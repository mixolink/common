package com.amituofo.common.kit.value;

import com.amituofo.common.api.ValueGetter;

public class CacheValue<T> extends TimelinessValue<T> {

	private long lastGetTime = 0;

	public CacheValue(long lifetime, ValueGetter<T> valueGatter) {
		super(lifetime, valueGatter);
	}

	public CacheValue(long lifetime) {
		super(lifetime);
	}

	public CacheValue(T value, int lifetime) {
		super(value, lifetime);
	}

	public CacheValue(T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		super(defaultValue, lifetime, valueGatter);
	}

	public CacheValue(T value, T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		super(value, defaultValue, lifetime, valueGatter);
	}

	public boolean isExpired() {
		if (lifetime <= 0) {
			return false;
		}

		return (System.currentTimeMillis() - Math.max(lastSetTime, lastGetTime)) > lifetime;
	}

	public T getValue(boolean autoUpdate) {
		lastGetTime = System.currentTimeMillis();

		return super.getValue(autoUpdate);
	}
}
