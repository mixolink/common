package com.amituofo.common.kit.value;

import java.util.HashMap;
import java.util.Map;

import com.amituofo.common.api.Callback;
import com.amituofo.common.api.ValueGetter;

public class TimelinessMap<T> {
	private final long lifetime;
	private final T defaultValue;

	private final ValueGetter<T> valueGatter;
	private Map<String, TimelinessValue<T>> valueMap = new HashMap<>();
	private Callback<T> expiredCallback;

	public TimelinessMap() {
		this(null, -1, null);
	}

	public TimelinessMap(long lifetime) {
		this(null, lifetime, null);
	}

	public TimelinessMap(long lifetime, Callback<T> expiredCallback) {
		this(null, lifetime, null, expiredCallback);
	}

//	public TimelinessValue(T defaultValue, int lifetime) {
//		this(defaultValue, defaultValue, lifetime, null);
//	}

	public TimelinessMap(long lifetime, ValueGetter<T> valueGatter) {
		this(null, lifetime, valueGatter, null);
	}

	public TimelinessMap(T defaultValue, long lifetime, ValueGetter<T> valueGatter) {
		this(defaultValue, lifetime, valueGatter, null);
	}

	public TimelinessMap(T defaultValue, long lifetime, ValueGetter<T> valueGatter, Callback<T> expiredCallback) {
		this.defaultValue = defaultValue;
		this.lifetime = lifetime;
		this.valueGatter = valueGatter;
		this.expiredCallback = expiredCallback;
	}

	public T get(String key) {
		TimelinessValue<T> v;
		synchronized (valueMap) {
			v = valueMap.get(key);
			if (v == null) {
				if (valueGatter == null) {
					return null;
				}

				v = new TimelinessValue<T>(defaultValue, lifetime, valueGatter);
				valueMap.put(key, v);
			}
		}
		return v.getValue();
	}

	public void put(String key, T value) {
		set(key, value, lifetime);
	}

	public void set(String key, T value) {
		set(key, value, lifetime);
	}

	public void put(String key, T value, long lifetime) {
		set(key, value, lifetime);
	}

	public void set(String key, T value, long lifetime) {
//		TimelinessValue<T> v = new TimelinessValue<T>(value, lifetime, valueGatter);
//		valueMap.put(key, v);

		TimelinessValue<T> v;
		synchronized (valueMap) {
			v = valueMap.get(key);
			if (v == null) {
				v = new TimelinessValue<T>(defaultValue, lifetime, valueGatter);
				valueMap.put(key, v);
			}
		}

		v.setValue(value);
	}

	public T remove(String key) {
		TimelinessValue<T> v;
		synchronized (valueMap) {
			v = valueMap.get(key);
		}

		if (v == null) {
			return null;
		}

		return v.getValue();
	}
	
//	public boolean containsKey(String key) {
//		
//	}

	public boolean isExpired(String key) {
		TimelinessValue<T> v;
		synchronized (valueMap) {
			v = valueMap.get(key);
			if (v == null) {
				return true;
			}
		}

		if (v.isExpired()) {
			if (expiredCallback != null) {
				expiredCallback.callback(v.getValue());
			}
			synchronized (valueMap) {
				valueMap.remove(key);
			}
			return true;
		}

		return false;
	}

	public int size() {
		synchronized (valueMap) {
			return valueMap.size();
		}
	}

	public void clear() {
		Map<String, TimelinessValue<T>> valueMapOld = valueMap;
		valueMap = new HashMap<>();
		valueMapOld.clear();
	}

	public int clearAllExpired() {
		String[] keys;
		synchronized (valueMap) {
			if (valueMap.size() == 0) {
				return 0;
			}

			keys = valueMap.keySet().toArray(new String[valueMap.size()]);
		}

		int n = 0;
		for (String key : keys) {
			TimelinessValue<T> v = valueMap.get(key);
			if (v != null) {
				if (v.isExpired()) {
					valueMap.remove(key);
					n++;
				}
			}
		}

		return n;
	}

}
